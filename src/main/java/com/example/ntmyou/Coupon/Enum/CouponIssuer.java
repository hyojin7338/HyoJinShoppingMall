package com.example.ntmyou.Coupon.Enum;

public enum CouponIssuer {
    SITE("사이트 제공 쿠폰"),
    MASTER("마스터 제공 쿠폰");
    private final String description;
    CouponIssuer(String description) {
        this.description = description;
    }

}
