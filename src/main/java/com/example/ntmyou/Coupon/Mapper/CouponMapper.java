package com.example.ntmyou.Coupon.Mapper;

import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.Coupon.DTO.CouponRequestDto;
import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Product.Entity.Product;

public class CouponMapper {
    // CouponRequestDto -> Entity
    public static Coupon toEntity(CouponRequestDto dto, Master master, Product product) {
        return Coupon.builder()
                .name(dto.getName())
                .discountType(dto.getDiscountType())
                .discountValue(dto.getDiscountValue())
                .minOrderAmount(dto.getMinOrderAmount())
                .maxDiscountAmount(dto.getMaxDiscountAmount())
                .issuedBy(dto.getIssuedBy())
                .master(master)  // 판매자가 있으면 연결
                .product(product) // 특정 상품 전용이면 연결
                .startDay(dto.getStartDay())
                .endDay(dto.getEndDay())
                .isUse(true) // 기본적으로 사용 가능
                .isAutoIssued(true) // 회원가입시 자동으로 지급 그러니까 사용가능  TRUE
                .build();
    }

    // Entity -> CouponResponseDto
    public static CouponResponseDto toResponseDto(Coupon coupon) {
        return CouponResponseDto.builder()
                .couponId(coupon.getCouponId())
                .name(coupon.getName())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minOrderAmount(coupon.getMinOrderAmount())
                .maxDiscountAmount(coupon.getMaxDiscountAmount())
                .issuedBy(coupon.getIssuedBy())
                .masterName(coupon.getMaster() != null ? coupon.getMaster().getName() : "사이트 제공")
                .productName(coupon.getProduct() != null ? coupon.getProduct().getName() : "모든 상품 적용")
                .startDay(coupon.getStartDay())
                .endDay(coupon.getEndDay())
                .isUse(coupon.getIsUse())
                .isAutoIssued(coupon.getIsAutoIssued())
                .build();
    }



}
