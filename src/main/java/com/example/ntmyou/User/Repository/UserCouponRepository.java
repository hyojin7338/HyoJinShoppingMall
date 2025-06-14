package com.example.ntmyou.User.Repository;


import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByUserAndCoupon(User user, Coupon coupon);

    List<UserCoupon> findByUser_UserId(Long userId);

    //특정 유저가 보유한 사용 가능한 쿠폰 조회
    List<UserCoupon> findByUser_UserIdAndIsUsedFalse(Long userId);

    Optional findByUser(User user);


    // 생일인 유저 확인하여 쿠폰 지급
    boolean existsByUserAndCoupon_NameAndReceivedBetween(User user, String couponName,
                                                          LocalDateTime start, LocalDateTime end);
}
