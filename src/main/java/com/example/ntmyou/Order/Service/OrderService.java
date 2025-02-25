package com.example.ntmyou.Order.Service;

import com.example.ntmyou.Exception.ProductNotFoundException;
import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.Order.Entity.Order;
import com.example.ntmyou.Order.Entity.OrderItem;
import com.example.ntmyou.Order.Enum.OrderStatus;
import com.example.ntmyou.Order.Repository.OrderRepository;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
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

    @Transactional
    public Order createOrder(Long userId, List<Long> productIds) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 유저입니다."));

        // 상품리스트 순회 & 주문 아이템 생성하기
        List<OrderItem> orderItems = new ArrayList<>();
        int totalPrice = 0; // 총 주문 가격 초기화
        int shippingFee = 3000; // 기본 배송비 3000원

        // 사용자가 주문한 상품 목록을 순회하며 상품 정보를 조회
        for(Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

            OrderItem orderItem = OrderItem.builder()
                    .order(null) // Order가 아직 생성되지 않았으므로 null 저장
                    .product(product)
                    .qty(1) // 기본적으로 1개 구마한다고 가정
                    .itemPrice(product.getAmount())
                    .totalPrice(product.getAmount())
                    .build();

            totalPrice += orderItem.getTotalPrice(); // 주문 총 가격에 상품 가격 추가
            // add는 어디서 가지고 왔는가??

            orderItems.add(orderItem);
        }

        // 총 주문 가격에 배송비 추가

        totalPrice += shippingFee;

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .totalPrice(totalPrice)
                .shippingFee(shippingFee)
                .orderStatus(OrderStatus.ORDERED)
                .build();

        // OrderItem과 Order의 관계 설정
        // 주문 아이템과 주문 연결하기
        for (OrderItem orderItem : orderItems){
            orderItem.setOrder(order);
        }

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrderByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        return orderRepository.findAllByUser(user);
    }
}
