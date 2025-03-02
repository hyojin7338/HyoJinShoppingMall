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
    private Long userId; // 로그인 할 때 가지고 다녀야겠다
    private String name;   // 닉네임
    private Long cartId; // 장바구니Id
    private String accessToken;
    private String refreshToken;
}
