package com.example.ntmyou.Order.Service;


import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.Order.Dto.OrderItemRequestDto;
import com.example.ntmyou.Order.Dto.OrderRequestDto;
import com.example.ntmyou.Order.Dto.OrderResponseDto;
import com.example.ntmyou.Order.Entity.Order;
import com.example.ntmyou.Order.Entity.OrderItem;

import com.example.ntmyou.Order.Mapper.OrderMapper;
import com.example.ntmyou.Order.Repository.OrderRepository;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.Product.Repository.ProductSizeRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ProductSizeRepository productSizeRepository;

    @Transactional(readOnly = true)
    public List<Order> getOrderByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        return orderRepository.findAllByUser(user);
    }


}
