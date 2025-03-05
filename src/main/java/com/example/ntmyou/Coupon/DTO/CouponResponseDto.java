package com.example.ntmyou.Coupon.DTO;

import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponseDto {
    private Long userCouponId; // 추가 2025-03-05
    private Long couponId;
    private String name; // 쿠폰명
    private DiscountType discountType; // 할인방식 (퍼센트 or xxx 원)
    private Integer discountValue; // 할인 값 (10% or 3000원)
    private Integer minOrderAmount; // 최소 주문 금액
    private Integer maxDiscountAmount; // 퍼센트 할인의 최대 할인 금액  (최대 30000만원)
    private CouponIssuer issuedBy; // 쿠폰발급처 ( 사이트 / 판매자 )
    private String masterName; // 판매자 쿠폰일 경우 판매자 ID (NULL 값도 가능)
    private String productName; // 특정 상품에만 적용할 경우 해당 상품ID
    private LocalDateTime startDay; // 쿠폰시작일
    private LocalDateTime endDay; // 쿠폰 종료일
    private Boolean isUse; // 쿠폰 사용 여부
    private Boolean isAutoIssued; // 자동쿠폰여부 // true 지급 완료
}
