package com.example.ntmyou.Order.Controller;

import com.example.ntmyou.Order.Dto.OrderRequestDto;
import com.example.ntmyou.Order.Dto.OrderResponseDto;
import com.example.ntmyou.Order.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/{userId}")
    public ResponseEntity<OrderResponseDto> createOrder(
            @PathVariable Long userId,
            @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto orderResponseDto = orderService.createOrder(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

}
