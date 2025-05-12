package com.example.ntmyou.User.Controller;

import com.example.ntmyou.User.DTO.*;
import com.example.ntmyou.User.Repository.UserRepository;
import com.example.ntmyou.User.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {


    private final UserService userService;
    private final UserRepository userRepository;

    // 회원가입 // 회원가입 시 기본 배송정보를 불러오고, 사용자가 변경할 수 있도록 수정 // 2025-03-06
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> createSignup
            (@RequestBody @Valid UserSignupRequestDto requestDto) {
        UserSignupResponseDto responseDto = userService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 회원가입 이메일 중복 확인
    @GetMapping("/codeCheck")
    public ResponseEntity<?> emailCheck(@RequestParam String code) {
        boolean exists = userRepository.existsByCode(code);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 코드 입니다.");
        }
        return ResponseEntity.ok("SUCCESS");
    }

    // 회원가입 닉네임 중복 확인
    @GetMapping("/nameCheck")
    public ResponseEntity<?> nickName(@RequestParam String name) {
        boolean exists = userRepository.existsByName(name);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 닉네임 입니다.");
        }
        return ResponseEntity.ok("SUCCESS");
    }


    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        UserLoginResponseDto loginResponseDto = userService.login(requestDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    // 프로필 등록
    @PostMapping("/update/{userId}/profile")
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            @PathVariable Long userId,
            @RequestPart(value = "requestDto") @Valid UserProfileRequestDto requestDto,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "subImages", required = false) List<MultipartFile> subImages) {

        UserProfileResponseDto responseDto = userService.profileCreate(userId, requestDto, mainImage, subImages);

        return ResponseEntity.ok(responseDto);
    }


}
