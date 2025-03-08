package com.example.ntmyou.Address;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddressResponseDto {
    private Long addressId;
    private String address;
    private String region;
    private String receiverName;
    private String receiverTel;
    private boolean isDefault; // 기본 배송지 여부
}
