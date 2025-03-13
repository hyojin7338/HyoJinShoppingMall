package com.example.ntmyou.Order.Mapper;

import com.example.ntmyou.Order.Dto.OrderItemRequestDto;
import com.example.ntmyou.Order.Dto.OrderItemResponseDto;
import com.example.ntmyou.Order.Dto.OrderRequestDto;
import com.example.ntmyou.Order.Dto.OrderResponseDto;
import com.example.ntmyou.Order.Entity.Order;
import com.example.ntmyou.Order.Entity.OrderItem;

import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;
import com.example.ntmyou.User.Entity.User;

import java.util.ArrayList;

import java.util.stream.Collectors;

public class OrderMapper {
    // Order request -> entity
    public Order toEntity(OrderRequestDto dto, User user) {
        return Order.builder()
                .user(user)
                .shippingFee(dto.getShippingFee())
                .orderItems(new ArrayList<>())
                .totalPrice(0)
                .build();
    }


    // Entity -> ResponseDto
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

    public OrderItem toEntity(OrderItemRequestDto dto
            , Product product
            , ProductSize productSize
            , Order order) {
        return OrderItem.builder()
                .product(product)
                .productSize(productSize)
                .order(order)
                .qty(dto.getQty())
                .itemPrice(dto.getItemPrices())
                .totalPrice((dto.getItemPrices() != null ? dto.getItemPrices() : 0) * dto.getQty())
                .build();
    }

    public OrderItemResponseDto toDto(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProduct().getProductId())
                .productSizeId(orderItem.getProductSize().getProductSizeId())
                .qty(orderItem.getQty())
                .itemPrices(orderItem.getItemPrice())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }


}
