package com.example.ntmyou.User.Mapper;

import com.example.ntmyou.User.DTO.UserSignupRequestDto;
import com.example.ntmyou.User.DTO.UserSignupResponseDto;
import com.example.ntmyou.User.Entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserMapper {
    // 회원가입    //requestDto → Entity
    public static User toEntity(UserSignupRequestDto requestDto, BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .code(requestDto.getCode())
                .name(requestDto.getName())
                .password(passwordEncoder.encode(requestDto.getPassword())) // 여기서 암호화
                .gender(requestDto.getGender())
                .zipCode(requestDto.getZipCode())
                .address(requestDto.getAddress())
                .region(requestDto.getRegion())
                .tel(requestDto.getTel())  //  전화번호 추가
                .birthDay(requestDto.getBirthDay()) // 생년월일 추가 // 추후 생일쿠폰 지급
                .build();
    }

    //Entity →  responseDto
    public static UserSignupResponseDto toResponseDTO(User user) {
        return new UserSignupResponseDto(
                user.getCode(),
                user.getName(),
                user.getGender()
        );
    }



}

