package com.example.ntmyou.Order.Entity;

import com.example.ntmyou.Order.Entity.Order;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderItem_id", nullable = false)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_size_id", nullable = false)
    private ProductSize productSize;  // 선택한 사이즈 정보

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Integer itemPrice;

    @Column(nullable = false)
    private Integer totalPrice;

    @PrePersist
    private void prePersist() {
        this.totalPrice = this.itemPrice * this.qty;
    }
}
