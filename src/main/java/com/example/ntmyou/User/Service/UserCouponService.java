package com.example.ntmyou.User.Service;

import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import com.example.ntmyou.Coupon.Repository.CouponRepository;
import com.example.ntmyou.Exception.CouponNotFoundException;
import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.Exception.UserCouponAlreadyIssueException;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    //
    @Transactional
    public void issueSignupCouponToUser(User user) {
        //isAutoIssued = true 인 쿠폰 조회
        List<Coupon> autoCoupons = couponRepository.findByIsAutoIssuedTrue();

        // 만약 자동 발급 쿠폰이 없으면 회원가입 쿠폰을 자동 생성
        if (autoCoupons.isEmpty()) {
            Coupon signupCoupon = Coupon.builder()
                    .name("회원가입 5% 할인 쿠폰")
                    .discountType(DiscountType.PERCENT)
                    .discountValue(5) // 5% 할인
                    .minOrderAmount(10000)
                    .maxDiscountAmount(5000)
                    .issuedBy(CouponIssuer.SITE)
                    .startDay(LocalDateTime.now())
                    .endDay(LocalDateTime.now().plusMonths(6))
                    .isUse(true)
                    .isAutoIssued(true) // 신규 가입자 자동 지급
                    .build();

            // 쿠폰 저장 후 리스트에 추가
            signupCoupon = couponRepository.save(signupCoupon);
            autoCoupons = List.of(signupCoupon);
        }

        for (Coupon coupon : autoCoupons) {
            UserCoupon userCoupon = UserCoupon.builder()
                    .user(user)
                    .coupon(coupon)
                    .isUsed(false)
                    .received(LocalDateTime.now()) // 받은 날짜
                    .build();
            userCouponRepository.save(userCoupon);
        }
    }

    // 특정 사용자에게 지급하는 쿠폰
    @Transactional
    public void issueCouponToUser(Long userId, Long couponId) {
        //  사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));
        // 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("존재하지 않는 쿠폰 입니다."));
        // 쿠폰 중복 조회
        boolean alreadyIssued = userCouponRepository.existsByUserAndCoupon(user, coupon);
        if (alreadyIssued) {
            throw new UserCouponAlreadyIssueException("이미 발급된 쿠폰입니다.");
        }
        // 유저쿠폰 생성 및 저장
        UserCoupon userCoupon = UserCoupon.builder()
                .user(user)
                .coupon(coupon)
                .isUsed(false) // 미사용 상태
                .received(LocalDateTime.now()) // 발급 된 시간
                .build();

        userCouponRepository.save(userCoupon);

    }

}
