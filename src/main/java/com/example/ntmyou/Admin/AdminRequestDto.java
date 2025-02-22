package com.example.ntmyou.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AdminRequestDto {
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
}
