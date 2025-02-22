package com.example.ntmyou.Master.DTO;


import lombok.Getter;

import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MasterProfileRequestDto {
    private String mainImg;  // 대표 이미지
    private List<String > imageUrls; // 서브 이미지

    private String name; // 닉네임 변경 가능
    private String zipCode; // 우편번호 수정 가능
    private String address; // 주소
    private String region; // 상세주소
    private String tel; // 전화번호
}
