package com.example.ntmyou.Address;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddressRequestDto {
    private String address; // 주소
    private String region; // 상세주소
    private String receiverName; // 받는사람
    private String receiverTel; // 받는사람 전화번호
    private boolean isDefault; // 기본 배송지
}
