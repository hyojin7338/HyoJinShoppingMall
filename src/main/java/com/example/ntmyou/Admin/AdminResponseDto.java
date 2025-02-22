package com.example.ntmyou.Admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AdminResponseDto {
    private Long adminId;
    private String code;
    private String name;
}
