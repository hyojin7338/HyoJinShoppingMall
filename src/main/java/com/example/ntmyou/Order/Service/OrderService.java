package com.example.ntmyou.Order.Service;


import com.example.ntmyou.Exception.OrderNotFoundException;
import com.example.ntmyou.Exception.ProductNotFoundException;
import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.Order.Dto.OrderItemRequestDto;
import com.example.ntmyou.Order.Dto.OrderItemResponseDto;
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

import java.util.List;



@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ProductSizeRepository productSizeRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, User user) {
        // 1️⃣ 새로운 주문 생성
        Order order = orderMapper.toEntity(requestDto, user);

        int totalOrderPrice = 0;

        // 2️⃣ 주문 아이템 처리
        for (OrderItemRequestDto itemDto : requestDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            ProductSize productSize = productSizeRepository.findById(itemDto.getProductSizeId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품의 사이즈 정보를 찾을 수 없습니다."));

            // 3️⃣ 상품 재고 확인 및 감소 처리
            if (productSize.getCnt() < itemDto.getQty()) {
                throw new IllegalStateException("재고가 부족합니다.");
            }
            productSize.setCnt(productSize.getCnt() - itemDto.getQty()); // 재고 감소

            // 4️⃣ 주문 아이템 생성 및 추가
            int totalPrice = itemDto.getItemPrice() * itemDto.getQty(); // 개별 총 가격
            totalOrderPrice += totalPrice; // 주문 총 가격 합산

            OrderItem orderItem = orderMapper.toEntity(itemDto, product, productSize, order, totalPrice);
            order.getOrderItems().add(orderItem);
        }

        // 5️⃣ 총 가격 및 주문 저장
        order.setTotalPrice(totalOrderPrice + requestDto.getShippingFee());
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);

    }
    @Transactional(readOnly = true)
    public List<Order> getOrderByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        return orderRepository.findAllByUser(user);
    }


}
