package com.example.ntmyou.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {
    private String name;   // 닉네임
    private String accessToken;
    private String refreshToken;
}
