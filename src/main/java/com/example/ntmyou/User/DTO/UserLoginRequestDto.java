package com.example.ntmyou.User.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String code;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
