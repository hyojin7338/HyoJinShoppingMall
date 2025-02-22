package com.example.ntmyou.Coupon.DTO;

import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponRequestDto {

    @NotBlank(message = "쿠폰명을 입력해 주세요.")
    private String name; // 쿠폰명 (예: "10% 할인 쿠폰")

    @NotNull(message = "할인 방식을 입력해 주세요.")
    private DiscountType discountType;  // 할인 방식 (PERCENT, FIXED)

    @NotNull(message = "할인 값을 입력해 주세요.")
    private Integer discountValue;  // 할인 값 (10% or 3000원)

    @NotNull(message = "최소 주문 금액을 입력해 주세요.")
    private Integer minOrderAmount; // 최소 주문 금액

    @NotNull(message = "최대 할인 금액을 입력해 주세요.")
    private Integer maxDiscountAmount; // 퍼센트 할인의 최대 할인 금액

    @NotNull(message = "쿠폰 발급처를 입력해 주세요.")
    private CouponIssuer issuedBy;  // 쿠폰 발급처 (사이트 / 판매자)

    private Long masterId;  // 판매자 쿠폰일 경우 판매자 ID

    private Long productId;  // 특정 상품에만 적용할 경우 해당 상품 ID

    @NotNull(message = "쿠폰 발급일을 입력해 주세요.")
    private LocalDateTime startDay;  // 쿠폰 발급일

    @NotNull(message = "쿠폰 만료일을 입력해 주세요.")
    private LocalDateTime endDay; // 쿠폰 만료일

}
