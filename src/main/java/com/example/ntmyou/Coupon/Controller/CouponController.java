package com.example.ntmyou.Coupon.Controller;

import com.example.ntmyou.Coupon.DTO.CouponRequestDto;
import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import com.example.ntmyou.Coupon.Service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    // 내 쿠폰함 조회
    @GetMapping("/master/coupon/findAll")
    public ResponseEntity<List<CouponResponseDto>> getCoupon() {
        return ResponseEntity.ok(couponService.getCoupon());
    }
}
