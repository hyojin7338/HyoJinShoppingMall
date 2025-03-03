package com.example.ntmyou.Coupon.Controller;

import com.example.ntmyou.Coupon.DTO.CouponRequestDto;
import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import com.example.ntmyou.Coupon.Service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    // 쿠폰 생성
    @PostMapping("/master/couponCreate")
    public ResponseEntity<CouponResponseDto> createCoupon(@RequestBody @Valid CouponRequestDto requestDto) {
        CouponResponseDto responseDto = couponService.createCoupon(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


     // 특정 유저의 쿠폰 조회 (사용자 ID 필요)
    @GetMapping("/coupons/{userId}")
    public ResponseEntity<List<CouponResponseDto>> getUserCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(couponService.getUserCoupons(userId)); // 특정 유저의 쿠폰 조회
    }

}
