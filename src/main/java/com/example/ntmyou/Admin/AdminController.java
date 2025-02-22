package com.example.ntmyou.Admin;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/admin/createAdmin")
    public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody @Valid AdminRequestDto requestDto) {
        AdminResponseDto responseDto = adminService.createAdmin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
