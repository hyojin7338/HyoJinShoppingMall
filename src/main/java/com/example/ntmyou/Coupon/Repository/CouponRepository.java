package com.example.ntmyou.Coupon.Repository;

import com.example.ntmyou.Coupon.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByName(String s);
    // 신규 회원에게 쿠폰 지급
    List<Coupon> findByIsAutoIssuedTrue();
}
