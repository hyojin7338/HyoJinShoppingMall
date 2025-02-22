package com.example.ntmyou.Cart.Entity;

import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 장바구니?

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>(); // 여러 개의 상품을 담을 수 있음

    @Column(nullable = false)
    private Integer totalPrice;  // 총 상품 가격 (할인 전)

    @Column(nullable = false)
    private Integer discountAmount;  // 적용된 할인 금액

    @Column(nullable = false)
    private Integer finalPrice = 0;  // 할인 적용 후 최종 가격

    @Column(nullable = false)
    private Integer shippingFee;  // 배송비 (ex: 3000원)

    @Column(nullable = false)
    private Boolean isCheckedOut;  // 결제 여부 (true면 결제 완료)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userCoupon_id")
    private UserCoupon appliedCoupon;  // 적용된 쿠폰 (사용자가 선택한 쿠폰)

    public void updateCartTotals() {
        // 아래와 같이 작성하면 가격 정보를 자동으로 계산하여 업데이트 하는 기능이다
        // stream으로 변환 -> mapToInt 가격 리스트로 변환  // .sum() 모든 가격 합산
        this.totalPrice = cartItems.stream().mapToInt(CartItem::getTotalPrice).sum();
        this.discountAmount = cartItems.stream().mapToInt(CartItem::getDiscountAmount).sum();
        this.finalPrice = totalPrice - discountAmount + shippingFee;
    }

}
