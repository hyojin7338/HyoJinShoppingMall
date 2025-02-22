package com.example.ntmyou.User.Entity;

import com.example.ntmyou.Coupon.Entity.Coupon;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userCoupon_id", nullable = false)
    private Long userCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 쿠폰을 받은 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;  // 어떤 쿠폰인지?

    @Column(nullable = false)
    private Boolean isUsed = false;  // 쿠폰 사용 여부 (true = 사용됨)

    @Column(nullable = false)
    private LocalDateTime received; // 쿠폰을 받은 날짜

    @Column
    private LocalDateTime useAt; // 쿠폰 사용 날짜

    public void useCoupon() {
        this.isUsed = true;
        this.useAt = LocalDateTime.now();
    }
}
