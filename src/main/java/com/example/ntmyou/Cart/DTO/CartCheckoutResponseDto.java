package com.example.ntmyou.Cart.DTO;

import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartCheckoutResponseDto {
    private Long cartId;
    private List<CartItemCheckDto> cartItems; // 장바구니에 담긴 상품 목록
    private int totalPrice; // 상품총가격
    private int shippingFee; // 배송비
    private int discountAmount; // 현재 할인된 금액
    private int finalPrice; // 최종 결제 금액
    private List<CouponResponseDto> availableCoupons; // 사용가능한 쿠폰
}

