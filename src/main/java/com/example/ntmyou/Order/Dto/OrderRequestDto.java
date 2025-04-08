package com.example.ntmyou.Order.Dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Long userId;  // 구매자 ID
    private List<OrderItemRequestDto> orderItems;  // 주문 상품 목록
    private Integer shippingFee; // 배송비
    private Long userCouponId; // 유저가 지급 받은 쿠폰 ID
}
