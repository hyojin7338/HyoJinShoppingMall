package com.example.ntmyou.User.DTO;

import com.example.ntmyou.Config.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupResponseDto {
    private String code; // 코드
    private String name; // 닉네임
    private Gender gender; // 성별
}
