package com.example.ntmyou.Master.Mapper;


import com.example.ntmyou.Master.DTO.MasterSignupRequestDto;
import com.example.ntmyou.Master.DTO.MasterSignupResponseDto;
import com.example.ntmyou.Master.Entity.Master;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MasterMapper {
    public static Master toEntity(MasterSignupRequestDto requestDto
            , BCryptPasswordEncoder passwordEncoder) {
        // 회원가입
        return Master.builder()
                .code(requestDto.getCode())
                .name(requestDto.getName())
                .businessName(requestDto.getBusinessName())
                .businessNo(requestDto.getBusinessNo())
                .tel(requestDto.getTel())
                .password(passwordEncoder.encode(requestDto.getPassword())) // 암호화 진행
                .zipCode(requestDto.getZipCode())
                .address(requestDto.getAddress())
                .region(requestDto.getRegion())
                .build();
    }

    public static MasterSignupResponseDto toResponseDto(Master master) {
        return new MasterSignupResponseDto(
                master.getCode(),
                master.getName(),
                master.getBusinessName()
        );
    }

}
