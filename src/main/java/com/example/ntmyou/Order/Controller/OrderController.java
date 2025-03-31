package com.example.ntmyou.Order.Controller;

import com.example.ntmyou.Order.Dto.OrderRequestDto;
import com.example.ntmyou.Order.Dto.OrderResponseDto;
import com.example.ntmyou.Order.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 단일 상품 결제
    @PostMapping("/order/{userId}")
    public ResponseEntity<OrderResponseDto> createOrder(
            @PathVariable Long userId,
            @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto orderResponseDto = orderService.createOrder(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    // 장바구니에서 상품 결제
    @PostMapping("/order/cart/{userId}")
    public ResponseEntity<OrderResponseDto> createOrderFromCart(@PathVariable Long userId) {
        OrderResponseDto orderResponseDto = orderService.createCartOrder(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @GetMapping("/order/getOrderByUser/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrderByUser(userId);
        return ResponseEntity.ok(orders);
    }


}
