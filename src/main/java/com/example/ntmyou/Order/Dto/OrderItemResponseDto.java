package com.example.ntmyou.Order.Dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long orderItemId;  // 주문 상품 ID
    private Long productId;  // 상품 ID
    private Long productSizeId; // 상품사이즈 ID
    private String productName; // 상품명
    private String size; // 상품 사이즈 
    private Integer qty;  // 수량
    private Integer itemPrice;  // 개별 가격
    private Integer totalPrice;  // 총 가격
}
