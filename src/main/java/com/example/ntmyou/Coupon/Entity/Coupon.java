package com.example.ntmyou.Coupon.Entity;

import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Product.Entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(nullable = false)
    private String name; // 쿠폰이름 (10% 할인쿠폰)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;  // 할인 방식 (PERCENT, FIXED)

    @Column(nullable = false)
    private Integer discountValue;  // 할인 값 (퍼센트일 경우 10, 정액일 경우 3000원 등)

    @Column(nullable = false)
    private Integer minOrderAmount; // 최소 주문 금액 (ex: 10000원 이상 구매 시 사용 가능)

    @Column(nullable = false)
    private Integer maxDiscountAmount; // 퍼센트 할인의 최대 할인 금액 (ex: 최대 5000원 할인)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponIssuer issuedBy;  // 쿠폰 발급처 (사이트 제공 / 판매자 제공)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Master master;  // 사이트가 발급한 경우는 판매자가 nullable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 특정 상품에만 적용 가능한 경우

    @Column(nullable = false)
    private LocalDateTime startDay;  // 쿠폰 발급일

    @Column(nullable = false)
    private LocalDateTime endDay; // 쿠폰 만료일

    @Column(nullable = false)
    private Boolean isUse;  // 쿠폰 사용 가능 여부 // true면 사용 가능

    @Column(nullable = false)
    private Boolean isAutoIssued; // true 지급완료
}
