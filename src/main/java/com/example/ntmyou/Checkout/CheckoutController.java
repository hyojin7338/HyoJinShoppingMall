package com.example.ntmyou.Checkout;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    // 단일 구매건 전 정보 확인하는 API
    @GetMapping("/product/{userId}/{productId}/{qty}")
    public ResponseEntity<CheckoutResponseDto> getCheckoutInfo(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @PathVariable Integer qty) {

        CheckoutResponseDto checkoutInfo = checkoutService.getCheckoutInfo(userId, productId, qty);
        return ResponseEntity.ok(checkoutInfo);
    }

    //  장바구니 담은 풀건 구매 전 정보 확인하는 API


}
