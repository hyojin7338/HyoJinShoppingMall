package com.example.ntmyou.Admin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AdminMapper {
    public static Admin toEntity(AdminRequestDto requestDto
            , BCryptPasswordEncoder passwordEncoder) {
        return Admin.builder()
                .code(requestDto.getCode())
                .name(requestDto.getName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
    }

    public static AdminResponseDto toResponseDto(Admin admin) {
        return AdminResponseDto.builder()
                .code(admin.getCode())
                .name(admin.getName())
                .build();
    }
}
