package com.example.ntmyou.Coupon.Enum;

public enum DiscountType {
    PERCENT("퍼센트 할인"),  // 퍼센트 할인 (ex: 10% 할인)
    FIXED("정액 할인");     // 정액 할인 (ex: 3000원 할인)

    private final String description;
    DiscountType(String description) {
        this.description = description;
    }


}
