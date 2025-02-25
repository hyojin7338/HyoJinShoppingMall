package com.example.ntmyou.Order.Controller;

import com.example.ntmyou.Order.Entity.Order;
import com.example.ntmyou.Order.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders/{userId}/create")
    public ResponseEntity<Order> createOrder(
            @PathVariable Long userId,
            @RequestBody List<Long> productIds
            )  {
        Order createdOrder = orderService.createOrder(userId, productIds);
        return ResponseEntity.ok(createdOrder);
    }
}
