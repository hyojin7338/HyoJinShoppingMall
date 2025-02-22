package com.example.ntmyou.Cart.Entity;

import com.example.ntmyou.Product.Entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
// (장바구니 내 개별 상품)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItem_id", nullable = false)
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // 어느 장바구니에 속하는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 어떤 상품인지

    @Column(nullable = false)
    private Integer qty;  // 상품 개수

    @Column(nullable = false)
    private Integer itemPrice;  // 상품 개별 가격 (할인 전)

    @Column(nullable = false)
    private Integer discountAmount;  // 개별 상품에 적용된 할인 금액

    @Column(nullable = false)
    private Integer totalPrice;  // 개별 상품 최종 가격

    // 개별 상품 총 가격 업데이트 (할인 적용 후)
    public void updateTotalPrice() {
        this.totalPrice = (this.itemPrice * this.qty) - this.discountAmount;
    }

    // 상품 수량 업데이트 // 상품 수량이 변경 되면 총 가격도 변경 되어야함
    public void updateQuantity(int qty) {
        this.qty = qty;
        updateTotalPrice();
    }

    // 개별 상품에 할인 적용 // 총 가격 재계산
    public void applyDiscount(int discountAmount) {
        this.discountAmount = discountAmount;
        updateTotalPrice();
    }

}
