package com.example.ntmyou.User.Repository;


import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByUserAndCoupon(User user, Coupon coupon);

    List<UserCoupon> findByUser_UserId(Long userId);
}
