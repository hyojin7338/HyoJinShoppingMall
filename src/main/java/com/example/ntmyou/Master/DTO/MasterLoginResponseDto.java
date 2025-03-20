package com.example.ntmyou.Master.DTO;

import com.example.ntmyou.Config.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterLoginResponseDto {
    private Long masterId;
    private String code;  // 아이디
    private String name; // 닉네임
    private String businessNo; // 사업자번호
    private String businessName; // 사업자명
    private Role role;

    private String accessToken;
    private String refreshToken;

}
