package com.example.ntmyou.Order.Entity;

import com.example.ntmyou.Order.Enum.OrderStatus;
import com.example.ntmyou.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // orderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 총 가격 (모든 주문 상품의 가격 합산)
    @Column(nullable = false)
    private Integer totalPrice;

    // 배송비
    @Column(nullable = false)
    private Integer shippingFee;

    // 구매 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    // 구매 날짜
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();  // 주문 시간 자동 설정
        this.orderStatus = OrderStatus.ORDERED;  // 기본적으로 "결제 완료" 상태로 설정
    }
}
