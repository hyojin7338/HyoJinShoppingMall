package com.example.ntmyou.Cart.DTO;

import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
// 프론트 주고 받을 때 깔끔하기 정리하기 위해서 추가 함 2025-03-02
public class CartResponseDto {
    private Long cartId; // 장바구니 ID
    private List<CartItemDto> cartItems; // 장바구니에 담긴 상품 목록
    private Integer totalPrice; // 총 가격 (할인 전)
    private Integer discountAmount; // 총 할인 금액
    private Integer shippingFee; // 배송비
    private Integer finalPrice; // 최종 결제 금액 (할인 후)
    private CouponResponseDto appliedCoupon; //
}
