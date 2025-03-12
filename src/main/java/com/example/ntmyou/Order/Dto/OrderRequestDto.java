package com.example.ntmyou.Order.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class OrderRequestDto {
    private Long orderId;  // 주문 ID
    private Long userId;  // 구매자 ID
    private List<OrderItemResponseDto> orderItems;  // 주문 상품 목록
    private Integer totalPrice;  // 총 가격
    private Integer shippingFee;  // 배송비
    private String orderStatus;  // 주문 상태
    private LocalDateTime orderDate;  // 주문 생성 날짜
}
