package com.example.ntmyou.Order.Mapper;

import com.example.ntmyou.Order.Dto.OrderItemRequestDto;
import com.example.ntmyou.Order.Dto.OrderItemResponseDto;
import com.example.ntmyou.Order.Dto.OrderRequestDto;
import com.example.ntmyou.Order.Dto.OrderResponseDto;
import com.example.ntmyou.Order.Entity.Order;
import com.example.ntmyou.Order.Entity.OrderItem;
import com.example.ntmyou.Order.Enum.OrderStatus;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.User.Entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    // Order request -> entity
    public static Order toEntity(OrderRequestDto requestDto
            , User user
            , List<OrderItem> orderItems
            , Integer totalPrice) {
        return Order.builder()
                .user(user)
                .orderItems(orderItems)
                .totalPrice(totalPrice)
                .shippingFee(requestDto.getShippingFee())
                .orderStatus(OrderStatus.ORDERED) // 결제완료
                .orderDate(requestDto.getOrderDate()) // 결제완료 날짜
                .build();
    }

    // OrderItem Request -> Entity
    public static OrderItem toEntity(OrderItemRequestDto requestDto
            , Product product
            , Order order) {
        return OrderItem.builder()
                .product(product)
                .order(order)
                .qty(requestDto.getQty())
                .itemPrice(requestDto.getItemPrice())
                .totalPrice(requestDto.getItemPrice() * requestDto.getQty())
                .build();
    }

    // Entity -> ResponseDto
    public static OrderResponseDto toDto(Order order) {
        List<OrderItemResponseDto> orderItems = order.getOrderItems().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .orderItems(orderItems)
                .totalPrice(order.getTotalPrice())
                .shippingFee(order.getShippingFee())
                .orderStatus(order.getOrderStatus().name())
                .orderDate(order.getOrderDate())
                .build();
    }

    public static OrderItemResponseDto toDto(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProduct().getProductId())
                .qty(orderItem.getQty())
                .itemPrice(orderItem.getItemPrice())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }


}
