package com.example.ntmyou.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequestDto {

    private String mainImg;  // 대표 이미지
    private List<String > images; // 서브 이미지


    private String zipCode; // 우편번호
    private String address; // 주소
    private String region; // 상세주소

    private String tel; //
}
