package com.example.ntmyou.Order.Dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;  // 주문 ID
    private Long userId;  // 구매자 ID
    private Integer totalPrice;  // 총 가격
    private Integer shippingFee;  // 배송비
    private String orderStatus;  // 주문 상태
    private LocalDateTime orderDate;  // 주문 생성 날짜
    private List<OrderItemResponseDto> orderItems;  // 주문 상품 목록
    private String userCouponName; // 유저가 사용한 쿠폰 이름
}
