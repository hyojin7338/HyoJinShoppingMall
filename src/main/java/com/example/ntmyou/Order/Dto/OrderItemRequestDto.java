package com.example.ntmyou.Order.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderItemRequestDto {
    @NotNull
    private Long productId;  // 상품 ID

    @NotNull
    private Integer qty;  // 주문 수량

    @NotNull
    private Integer itemPrice;  // 상품 개별 가격
}
