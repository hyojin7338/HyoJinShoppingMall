package com.example.ntmyou.Order.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class OrderRequestDto {
    private Long userId;  // 구매자 ID
    private List<OrderItemResponseDto> orderItems;  // 주문 상품 목록
    private Integer shippingFee; // 배송비
}
