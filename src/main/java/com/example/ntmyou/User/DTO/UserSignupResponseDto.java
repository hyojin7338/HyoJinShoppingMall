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
    //private String address; // 지역 -> 개인정보 수정에서 추가 할 수 있게 수정
    private Integer age; // 나이
}
