package com.example.ntmyou.Order.Enum;

public enum OrderStatus {
    ORDERED("결제 완료"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("주문 취소");
    private String description;
    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
