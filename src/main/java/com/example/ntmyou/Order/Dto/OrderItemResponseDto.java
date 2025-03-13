package com.example.ntmyou.Order.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrderItemResponseDto {
    private Long orderItemId;  // 주문 상품 ID
    private Long productId;  // 상품 ID
    private Long productSizeId; // 상품사이즈 ID
    private Integer qty;  // 수량
    private Integer itemPrices;  // 개별 가격
    private Integer totalPrice;  // 총 가격
}
