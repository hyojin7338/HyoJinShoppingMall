package com.example.ntmyou.Master.Controller;

import com.example.ntmyou.Master.DTO.*;
import com.example.ntmyou.Master.Service.MasterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController

@RequiredArgsConstructor
public class MasterController {

    private final MasterService masterService;

    // 관리자 회원가입
    @PostMapping("/master/signup")
    public ResponseEntity<MasterSignupResponseDto> createSignup
            (@RequestBody @Valid MasterSignupRequestDto requestDto) {
        MasterSignupResponseDto signupResponseDto =  masterService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponseDto);
    }

    // 관리자 로그인
    @PostMapping("/master/login")
    public ResponseEntity<MasterLoginResponseDto> masterLogin
    (@RequestBody @Valid MasterLoginRequestDto requestDto) {
        MasterLoginResponseDto loginResponseDto = masterService.login(requestDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    // 프로필등록
    @PostMapping("/update/{masterId}/profile")
    public ResponseEntity<MasterProfileResponseDto> masterUpdateProfile(
            @PathVariable Long masterId,
            @RequestPart(value = "requestDto") @Valid MasterProfileRequestDto requestDto,
            @RequestPart(value = "mainImage", required = false)MultipartFile mainImage,
            @RequestPart(value = "subImages", required = false) List<MultipartFile> subImages) {
        MasterProfileResponseDto responseDto = masterService.profileCreate(masterId, requestDto, mainImage, subImages);

        return ResponseEntity.ok(responseDto);
    }

}
