package com.example.ntmyou.Master.DTO;

import com.example.ntmyou.Config.Enum.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterSignupRequestDto {
    // 관리자 회원가입 시작
    @NotBlank(message = "코드를 입력해 주세요.")
    @Size(min = 6, max = 20, message = "코드는 6자 이상 20자 이하로 입력해야 합니다.")
    private String code; // 아이디

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 2, max = 8, message = "닉네임은 2자 이상 8글자 이하로 입력해야 합니다.")
    private String name; // 닉네임, 별칭

    @NotBlank(message = "사업자 상호명을 입력해주세요.")
    private String businessName; // 상호명

    @NotBlank(message = "사업자 번호를 입력해주세요.")
    private String businessNo; // 사업자번호

    @NotBlank(message = "전화번호를 입력해 주세요.")
    private String tel; // 전화번호

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(
            regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 8~20자로 영문, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password; // 비밀번호

    @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
    private String passwordConfirm;

    private String zipCode; // 우편번호 // null 값 가능
    private String address; // 주소 // null 값 가능
    private String region; // 상세주소 // null 값 가능

}
