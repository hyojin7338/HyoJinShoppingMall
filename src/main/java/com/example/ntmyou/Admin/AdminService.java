package com.example.ntmyou.Admin;

import com.example.ntmyou.Exception.AdminCodeExistsException;
import com.example.ntmyou.Exception.AdminNameExistsException;
import com.example.ntmyou.Exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //운영자 생성
    public AdminResponseDto createAdmin(AdminRequestDto requestDto) {
        // 운영자 중복 조회
        if (adminRepository.findByCode(requestDto.getCode()).isPresent()) {
            throw new AdminCodeExistsException("이미 존재하는 운영자입니다.");
        }

        // name 중복인지 확인
        if (adminRepository.findByName(requestDto.getName()).isPresent()) {
            throw new AdminNameExistsException("이미 존재하는 운영자이름입니다.");
        }

        // password 일치하는지 검증
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        Admin admin = AdminMapper.toEntity(requestDto, passwordEncoder);

        adminRepository.save(admin);

        return AdminMapper.toResponseDto(admin);
    }
}
