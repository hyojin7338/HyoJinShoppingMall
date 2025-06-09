package com.example.ntmyou.Coupon.Service;

import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import com.example.ntmyou.Coupon.Repository.CouponRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BirthdayCouponScheduler {

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 1 * *") // 매달 1일 00:00:00 실행
    //@Scheduled(cron = "0 * * * * *") // 매 1분마다 실행 (테스트용)
    public void issueBirthdayCoupons() {
        System.out.println("🎉 Birthday coupon scheduler triggered");

        int currentMonth = LocalDate.now().getMonthValue();

        // 생일인 사람을 찾아
        List<User> birthdayUsers = userRepository.findAll().stream()
                .filter(user -> user.getBirthDay() != null &&
                        user.getBirthDay().getMonthValue() == currentMonth)
                .collect(Collectors.toList());


        System.out.println("✅ 생일이 이번 달인 유저 수: " + birthdayUsers.size());

        // 해당 유저가 이번 년도에 생일 쿠폰을 받았는지 확인
        for(User user : birthdayUsers) {
            boolean alreadyIssued = userCouponRepository.existsByUserAndCoupon_NameAndReceivedBetween(
                    user,
                    "생일 축하 10% 할인 쿠폰",
                    LocalDateTime.now().withDayOfMonth(1),
                    LocalDateTime.now().withDayOfMonth(1).plusMonths(1)
            );

            System.out.println("👉 사용자: " + user.getName() + " / 이미 발급?: " + alreadyIssued);

            // 생일인 사람이 쿠폰이 없다면 -----> 쿠폰 생성
            if (!alreadyIssued) {
                Coupon coupon = Coupon.builder()
                        .name("생일 축하 10% 할인 쿠폰")
                        .discountType(DiscountType.PERCENT)
                        .discountValue(10)
                        .minOrderAmount(10000)
                        .maxDiscountAmount(20000)
                        .issuedBy(CouponIssuer.SITE)
                        .startDay(LocalDateTime.now())
                        .endDay(LocalDateTime.now().plusMonths(3))
                        .isUse(true)
                        .isAutoIssued(true)
                        .build();

                couponRepository.save(coupon);

                UserCoupon userCoupon = UserCoupon.builder()
                        .user(user)
                        .coupon(coupon)
                        .isUsed(false)
                        .received(LocalDateTime.now()) // 쿠폰 지급 받은 날짜
                        .build();

                userCouponRepository.save(userCoupon);
            }
        }


    }

}
