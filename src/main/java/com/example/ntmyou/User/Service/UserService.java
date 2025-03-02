package com.example.ntmyou.User.Service;

import com.example.ntmyou.Cart.Entity.Cart;
import com.example.ntmyou.Cart.Repository.CartRepository;
import com.example.ntmyou.Config.JWT.JwtToken;
import com.example.ntmyou.Config.JWT.JwtTokenProvider;
import com.example.ntmyou.Config.S3.AwsS3Service;
import com.example.ntmyou.Exception.*;
import com.example.ntmyou.User.DTO.*;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Mapper.UserMapper;
import com.example.ntmyou.User.Repository.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AwsS3Service awsS3Service;
    private final CartRepository cartRepository;

    private final UserCouponService userCouponService;

    // 입력 값이 null 또는 공백이 있으면 오류 발생
    private void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserNotEmpthException(fieldName + "은(는) 빈값이거나 공백이 존재합니다. 다시 입력해주세요.");
        }
    }

    // 회원가입
    @Transactional
    public UserSignupResponseDto create(UserSignupRequestDto dto) {
        validateNotEmpty(dto.getCode(), "아이디");
        // 중복 조회
        if (userRepository.findByCode(dto.getCode()).isPresent()) {
            throw new UserCodeAlreadyExistsException("존재하는 코드 입니다.");
        }

        //  중복 조회
        if (userRepository.findByName(dto.getName()).isPresent()) {
            throw new UserNameAlreadyExistsException("존재하는 닉네임 입니다.");
        }

        validateNotEmpty(dto.getName(), "닉네임");

        // 비밀번호 일치하는지 확인 진행
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");

        }

        validateNotEmpty(dto.getPassword(), "비밀번호");

        // Mapper를 사용하여 DTO → Entity 변환 (패스워드 암호화 포함)
        User user = UserMapper.toEntity(dto, passwordEncoder);

        // 유저 저장
        user = userRepository.save(user);

        //  회원가입 후 장바구니 자동 생성
        Cart cart = Cart.builder()
                .user(user) // ✅ 유저와 연결
                .totalPrice(0) // 초기 총 가격 0
                .discountAmount(0) // 초기 할인 금액 0
                .finalPrice(0) // 초기 최종 가격 0
                .shippingFee(3000) // 기본 배송비 3000원
                .isCheckedOut(false) // 결제 완료 여부 false
                .cartItems(new ArrayList<>()) // 빈 상품 리스트
                .build();

        cartRepository.save(cart); // ✅ 장바구니 저장

        userCouponService.issueSignupCouponToUser(user);

        // Entity → DTO 변환 후 반환
        return UserMapper.toResponseDTO(user);
    }

    // 프로필 수정
    @Transactional
    public UserProfileResponseDto profileCreate(Long userId, UserProfileRequestDto requestDto,
                                                MultipartFile mainImage, List<MultipartFile> subImages) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 코드입니다."));

        // 대표 이미지 업로드 처리 (예외 발생 시 기존 이미지 유지)
        String newMainImgUrl = user.getMainImgUrl(); // 기존 대표 이미지 유지
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
        List<String> newImageUrls = new ArrayList<>(user.getImageUrls()
                != null ? user.getImageUrls() : new ArrayList<>());
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
        user.updateProfileInfo(requestDto.getZipCode()
                , requestDto.getAddress()
                , requestDto.getRegion()
                , requestDto.getTel()
        );

        user.setMainImgUrl(newMainImgUrl);
        user.setImageUrls(newImageUrls);
        user.setZipCode(requestDto.getZipCode());
        user.setAddress(requestDto.getAddress());
        user.setRegion(requestDto.getRegion());
        user.setTel(requestDto.getTel());

        // 응답 객체 반환
        return new UserProfileResponseDto(
                user.getMainImgUrl(),
                user.getImageUrls(),
                user.getZipCode(),
                user.getAddress(),
                user.getRegion(),
                user.getTel()
        );
    }

    // 로그인
    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto dto) {

        // 입력값 검증 빈 값이거나 공백이 있을 때
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        // User 조회 (아이디로 변경)
        User user = userRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new UserLoginFailedException("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 비밀번호 검증 (User가 존재하더라도 동일한 메시지 반환)
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UserLoginFailedException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 휴먼 계정 여부 확인 (로그인 불가)
        if (Boolean.TRUE.equals(user.getCredit())) {
            throw new UserCreditException("휴먼 계정입니다. 로그인이 불가합니다.");
        }

        // ✅ cartId 가져오기 (User 엔티티에 Cart와 연관관계가 있어야 함)
        Long cartId = user.getCart() != null ? user.getCart().getCartId() : null;

        // JWT 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getCode(), null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return new UserLoginResponseDto(
                user.getUserId(),
                user.getName(),
                cartId,
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken()
        );
    }


}
