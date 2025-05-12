package com.example.ntmyou.User.Controller;

import com.example.ntmyou.User.Service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserCouponController {
    private final UserCouponService userCouponService;

    @PostMapping("/Master/{userId}/issue/{couponId}")
    public ResponseEntity<String> issueCouponToUser(
            @PathVariable Long userId,
            @PathVariable Long couponId) {
        userCouponService.issueCouponToUser(userId, couponId);
        return ResponseEntity.ok("쿠폰이 지급되었습니다.");
    }
}
