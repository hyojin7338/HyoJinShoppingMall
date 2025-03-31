package com.example.ntmyou.Cart.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long cartItemId; // 장바구니 아이템 ID
    private Long productId; // 상품 ID
    private Long productSizeId; // 상품사이즈ID
    private String size; // 상품 사이즈 S M L XL XXL
    private String productName; // 상품명
    private Integer productPrice; // 상품 가격
    private Integer qty; // 개별 상품 수량
}
