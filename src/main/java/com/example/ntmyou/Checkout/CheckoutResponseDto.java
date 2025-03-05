package com.example.ntmyou.Checkout;

import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CheckoutResponseDto {
    // 유저정보
    private Long userId;
    private String name;
    private String tel;
    private String address;
    private String region;

    // 상품정보
    private Long productId;
    private String productName;
    private Integer amount;
    private Integer cnt;
    private String mainImgUrl;
    private String businessName;

    // 사용가능한 쿠폰
    private List<CouponResponseDto> availableCoupons; // 사용 가능한 쿠폰 목록
}
