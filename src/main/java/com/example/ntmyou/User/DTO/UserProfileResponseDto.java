package com.example.ntmyou.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDto {
    private String mainImgUrl;  // 대표 이미지 URL
    private List<String> imageUrls; // 서브 이미지 URL
    private String zipCode; // 우편번호
    private String address; // 주소
    private String region; //  상세주서
    private String tel; // 전화번호

}
