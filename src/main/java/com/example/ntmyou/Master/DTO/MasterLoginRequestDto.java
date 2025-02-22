package com.example.ntmyou.Master.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterLoginRequestDto {
    private String code;  // 아이디
    private String businessNo; // 사업자번호
    private String password; // 비밀번호
}
