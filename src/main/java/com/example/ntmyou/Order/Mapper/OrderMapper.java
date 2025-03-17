package com.example.ntmyou.Order.Mapper;

import com.example.ntmyou.Order.Dto.OrderItemRequestDto;
import com.example.ntmyou.Order.Dto.OrderItemResponseDto;
import com.example.ntmyou.Order.Dto.OrderRequestDto;
import com.example.ntmyou.Order.Dto.OrderResponseDto;
import com.example.ntmyou.Order.Entity.Order;
import com.example.ntmyou.Order.Entity.OrderItem;

import com.example.ntmyou.Order.Enum.OrderStatus;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;
import com.example.ntmyou.User.Entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.stream.Collectors;

@Component
public class OrderMapper {
    // Order request -> entity
    public Order toEntity(OrderRequestDto dto, User user) {
        return Order.builder()
                .user(user)
                .shippingFee(dto.getShippingFee())
                .orderItems(new ArrayList<>()) // 초기화
                .totalPrice(0) // 초기값 설정
                .orderStatus(OrderStatus.ORDERED) // 명확하게 설정
                .orderDate(LocalDateTime.now()) // 주문 날짜 설정
                .build();
    }

    // Order 엔티티 -> OrderResponseDto 변환
    public OrderResponseDto toDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .totalPrice(order.getTotalPrice())
                .shippingFee(order.getShippingFee())
                .orderStatus(order.getOrderStatus().name())
                .orderDate(order.getOrderDate())
                .orderItems(order.getOrderItems().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    // OrderItemRequestDto -> OrderItem 엔티티 변환
    public OrderItem toEntity(OrderItemRequestDto dto, Product product, ProductSize productSize, Order order, Integer calculatedTotalPrice) {
        return OrderItem.builder()
                .product(product)
                .productSize(productSize)
                .order(order)
                .qty(dto.getQty())
                .itemPrice(dto.getItemPrice()) // 네이밍 통일
                .totalPrice(calculatedTotalPrice) // 서비스에서 계산하여 주입
                .build();
    }

    // OrderItem 엔티티 -> OrderItemResponseDto 변환
    // productSIzeId, productName, size 등 추가 2025-03-18
    public OrderItemResponseDto toDto(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProduct().getProductId())
                .productSizeId(orderItem.getProductSize().getProductSizeId())
                .productName(orderItem.getProduct().getName())
                .size(orderItem.getProductSize().getSize())
                .qty(orderItem.getQty())
                .itemPrice(orderItem.getItemPrice()) // 네이밍 통일
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }


}
