package com.example.ntmyou.User.DTO;

import com.example.ntmyou.Config.Enum.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestDto {

    @NotBlank(message = "코드를 입력해 주세요.")
    @Size(min = 6, max = 20, message = "코드는 6자 이상 20자 이하로 입력해야 합니다.")
    private String code;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 2, max = 8, message = "닉네임은 2자 이상 8글자 이하로 입력해야 합니다.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(
            regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 8~20자로 영문, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
    private String passwordConfirm;

    private Gender gender; // 성별

    private String zipCode; // 우편번호
    private String address; // 주소
    private String region; // 상세주소

    private String tel; // 전화번호
    private LocalDate birthDay; // 생년월일

}
