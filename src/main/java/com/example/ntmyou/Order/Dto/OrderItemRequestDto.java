package com.example.ntmyou.Order.Dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderItemRequestDto {

    private Long productId;  // 상품 ID
    private Long productSizeId; // 선택한 상품 사이즈 Id
    private Integer qty;  // 주문 수량
    private Integer itemPrice; // 상품 단가

}
