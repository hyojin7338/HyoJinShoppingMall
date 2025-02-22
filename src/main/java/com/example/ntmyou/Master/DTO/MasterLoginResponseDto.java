package com.example.ntmyou.Master.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterLoginResponseDto {
    private String code;  // 아이디
    private String name; // 닉네임
    private String businessNo; // 사업자번호
    private String businessName; // 사업자명

    private String accessToken;
    private String refreshToken;

}
