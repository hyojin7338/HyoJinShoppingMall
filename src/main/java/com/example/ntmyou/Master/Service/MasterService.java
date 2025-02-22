package com.example.ntmyou.Master.Service;

import com.example.ntmyou.Config.JWT.JwtToken;
import com.example.ntmyou.Config.JWT.JwtTokenProvider;
import com.example.ntmyou.Config.S3.AwsS3Service;
import com.example.ntmyou.Exception.*;
import com.example.ntmyou.Master.DTO.*;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Master.Mapper.MasterMapper;
import com.example.ntmyou.Master.Repository.MasterRepository;
import com.example.ntmyou.User.DTO.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterService {

    private final MasterRepository masterRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AwsS3Service awsS3Service;


    // 입력 값이 null 또는 공백이 있으면 오류 발생
    private void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserNotEmpthException(fieldName + "은(는) 빈값이거나 공백이 존재합니다. 다시 입력해주세요.");
        }
    }

    // 쇼핑몰 관리자 회원가입
    @Transactional
    public MasterSignupResponseDto create(MasterSignupRequestDto requestDto) {

        // 입력값 검증
        validateNotEmpty(requestDto.getCode(), "아이디");
        if (masterRepository.findByCode(requestDto.getCode()).isPresent()){
            throw new MasterCodeAlreadyExistsException("존재하는 코드입니다.");
        }

        validateNotEmpty(requestDto.getName(), "이름");
        if (masterRepository.findByName(requestDto.getName()).isPresent()){
            throw new MasterNameAlreadyExistsException("존재하는 닉네임입니다.");
        }

        validateNotEmpty(requestDto.getPassword(), "비밀번호");
        validateNotEmpty(requestDto.getPasswordConfirm(), "비밀번호 재확인");
        // 비밀번호 일치하는지 확인 진행
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");

        }

        validateNotEmpty(requestDto.getBusinessName(), "사업자명");


        if (masterRepository.findByBusinessNo(requestDto.getBusinessNo()).isPresent()) {
            throw new MasterBusinessNoAlreadyExistsException("이미 존재하는 사업자번호 입니다.");
        }
        validateNotEmpty(requestDto.getBusinessNo(), "사업자번호");

        Master master = MasterMapper.toEntity(requestDto, passwordEncoder);

        master = masterRepository.save(master);

        return MasterMapper.toResponseDto(master);

    }

    // 쇼핑몰 관리자 정보 수정
    @Transactional
    public MasterProfileResponseDto profileCreate(Long masterId, MasterProfileRequestDto profileRequestDto,
                                                  MultipartFile mainImage, List<MultipartFile> subImages)
    {
        // 관리자 조회
        Master master = masterRepository.findById(masterId)
                .orElseThrow(() -> new MasterNotFoundException("존재하지 않는 관리자 입니다."));


        validateNotEmpty(profileRequestDto.getName(), "이름");
        // 닉네임은 유니크하다
        if (masterRepository.findByName(profileRequestDto.getName()).isPresent()) {
            throw new MasterNameAlreadyExistsException("이미 존재하는 닉네임 입니다.");
        }

        // 대표 이미지 업로드 처리 (예외 발생 시 기존 이미지 유지)
        String newMainImgUrl = master.getMainImgUrl(); // 기존 대표 이미지 유지
        if (mainImage != null && !mainImage.isEmpty()) {
            try {
                List<String> uploadedMainImage = awsS3Service.uploadFile(List.of(mainImage));
                if (!uploadedMainImage.isEmpty()) {
                    // ✅ 기존 대표 이미지가 존재할 때만 삭제
                    if (newMainImgUrl != null && !newMainImgUrl.isEmpty()) {
                        awsS3Service.deleteFile(newMainImgUrl);
                    }
                    newMainImgUrl = uploadedMainImage.get(0);
                }
            } catch (Exception e) {
                throw new MainFileUploadException("대표 이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        }

        // 서브 이미지 업로드 처리 (예외 발생 시 기존 이미지 유지)
        List<String> newImageUrls = new ArrayList<>(master.getImageUrls()
                != null ? master.getImageUrls() : new ArrayList<>());
        if (subImages != null && !subImages.isEmpty()) {
            try {
                List<String> uploadedSubImages = awsS3Service.uploadFile(subImages);
                if (!uploadedSubImages.isEmpty()) {
                    // ✅ 기존 서브 이미지가 있을 경우만 삭제
                    if (!newImageUrls.isEmpty()) {
                        newImageUrls.forEach(awsS3Service::deleteFile);
                    }
                    newImageUrls = uploadedSubImages;
                }
            } catch (Exception e) {
                throw new SubFileUploadException("서브 이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        }

        // 프로필 정보 업데이트
        master.updateProfileInfo(profileRequestDto.getZipCode()
                , profileRequestDto.getAddress()
                , profileRequestDto.getRegion()
                , profileRequestDto.getTel()
        );

        master.setMainImgUrl(newMainImgUrl);
        master.setImageUrls(newImageUrls);
        master.setZipCode(profileRequestDto.getZipCode());
        master.setAddress(profileRequestDto.getAddress());
        master.setRegion(profileRequestDto.getRegion());
        master.setTel(profileRequestDto.getTel());

        // 응답 객체 반환
        return new MasterProfileResponseDto(
                master.getMainImgUrl(),
                master.getImageUrls(),
                master.getName(),
                master.getZipCode(),
                master.getAddress(),
                master.getRegion(),
                master.getTel()
        );
    }


    // 쇼핑몰 관리자 로그인
    @Transactional
    public MasterLoginResponseDto login(MasterLoginRequestDto requestDto) {
        // 공백 검증
        validateNotEmpty(requestDto.getCode(), "아이디 ");
        validateNotEmpty(requestDto.getPassword(), "비밀번호");

        // 마스터 코드로 조회 시 보안을 위하여 아이디 또는 비밀번호가 일치하지 않습니다 이렇게 나오게
        Master master = masterRepository.findByCode(requestDto.getCode())
                .orElseThrow(() -> new MasterLoginFailException("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 비밀번호 검증 (아이디가 존재하더라도 동일한 메시지 반환)
        if (!passwordEncoder.matches(requestDto.getPassword(), master.getPassword())) {
            throw new MasterLoginFailPasswordException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                master.getCode(), null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return new MasterLoginResponseDto(
                master.getCode(),
                master.getName(),
                master.getBusinessNo(),
                master.getBusinessName(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken()
        );

    }

}
