package com.example.ntmyou.Master.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MasterSignupResponseDto {
    private String code;
    private String name;
    private String businessName;
}
