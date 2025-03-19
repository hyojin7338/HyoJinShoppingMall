package com.example.ntmyou.User.DTO;

import com.example.ntmyou.Config.Enum.Role;
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
    // 운영자 문의사항건으로 쇼핑몰 판매자와 ROLE구분이 되어야 한다 // 2025-03-19
    private Role role;
}
