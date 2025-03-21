package com.example.ntmyou.Cart.Controller;

import com.example.ntmyou.Cart.DTO.CartResponseDto;
import com.example.ntmyou.Cart.Service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니에 상품 추가 API
    @PostMapping("/{cartId}/add-product/{productId}")
    public ResponseEntity<String> addProductToCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam int qty) {
        cartService.addProductToCart(cartId, productId, qty);
        return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
    }

    // 특정 유저가 본인 장바구니를 조회하는 API
    @GetMapping("/cart/{userId}")
    public ResponseEntity<CartResponseDto> getCartByUserId(@PathVariable Long userId) {
        CartResponseDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    // 장바구니에 쿠폰 적용 API
    @PostMapping("/cart/{cartId}/apply-coupon/{userCouponId}")
    public ResponseEntity<String> applyCouponToCart(
            @PathVariable Long cartId,
            @PathVariable Long userCouponId
    ) {
        cartService.applyCouponToCart(cartId, userCouponId);
        return ResponseEntity.ok("쿠폰이 장바구니에 적용되었습니다.");
    }

    // 장바구니 삭제
    @DeleteMapping("/cart/{cartId}/Delete/{productId}")
    public ResponseEntity<String> removeProductFromCart(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ) {
        cartService.removeProductFromCart(cartId, productId);
        return ResponseEntity.ok("장바구니에서 정상적으로 제거 되었습니다.");
    }

    // 장바구니 쿠폰 취소
    @DeleteMapping("/cart/{cartId}/remove-coupon")
    public ResponseEntity<String> removeCouponFromCart(@PathVariable Long cartId) {
        cartService.removeCouponFromCart(cartId);
        return ResponseEntity.ok("쿠폰이 장바구니에서 제거되었습니다.");
    }

    // 결제 API
    @PostMapping("/{cartId}/checkOut")
    public ResponseEntity<String> checkOut(@PathVariable Long cartId) {
        cartService.checkOut(cartId);
        return ResponseEntity.ok("결제가 완료되었습니다.");
    }
}
