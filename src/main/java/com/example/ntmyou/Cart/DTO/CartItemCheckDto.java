package com.example.ntmyou.Cart.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemCheckDto {
    private Long cartItemId;
    private String productName;
    private String size;
    private int qty;
    private int itemPrice;
}
