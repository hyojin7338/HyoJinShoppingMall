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

    // 구매 전 정보 확인하는 API
    @GetMapping("/product/{userId}/{productId}")
    public ResponseEntity<CheckoutResponseDto> getCheckoutInfo(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        CheckoutResponseDto checkoutInfo = checkoutService.getCheckoutInfo(userId, productId);
        return ResponseEntity.ok(checkoutInfo);
    }
}
