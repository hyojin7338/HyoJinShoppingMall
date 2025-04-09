package com.example.ntmyou.Order.Service;


import com.example.ntmyou.Cart.Entity.Cart;
import com.example.ntmyou.Cart.Entity.CartItem;
import com.example.ntmyou.Cart.Repository.CartRepository;
import com.example.ntmyou.Exception.*;
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
import com.example.ntmyou.Product.Service.ProductService;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final ProductSizeRepository productSizeRepository;
    private final CartRepository cartRepository;
    private final UserCouponRepository userCouponRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    // 상세페이지에서 구매하기
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 코드입니다,"));

        // 쿠폰을 사용하지 않을 수 있으니까 // 단일 결제건도 쿠폰 사용 추가  // 쿠폰 사용 처리 // 2025-04-08
        UserCoupon userCoupon = null;
        if (requestDto.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findById(requestDto.getUserCouponId())
                    .orElseThrow(() -> new UserCouponNotFoundException("쿠폰을 찾을 수 없습니다."));

            if (!userCoupon.getUser().getUserId().equals(userId)) {
                throw new IllegalArgumentException("해당 쿠폰은 사용자의 쿠폰이 아닙니다.");
            }

            if (userCoupon.getIsUsed()) {
                throw new IllegalArgumentException("이미 사용한 쿠폰입니다.");
            }
        }

        // 새로운 주문 생성
        Order order = orderMapper.toEntity(requestDto, user, userCoupon);

        logger.info("주문 생성 확인 >> orderId={}, 쿠폰ID={}, 쿠폰명={}, 사용여부={}",
                order.getOrderId(),
                order.getAppliedCoupon() != null ? order.getAppliedCoupon().getUserCouponId() : "없음",
                order.getAppliedCoupon() != null ? order.getAppliedCoupon().getCoupon().getName() : "없음",
                order.getAppliedCoupon() != null ? order.getAppliedCoupon().getIsUsed() : "N/A"
        );

        int totalOrderPrice = 0;

        // 주문 아이템 처리
        for (OrderItemRequestDto itemDto : requestDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            ProductSize productSize = productSizeRepository.findById(itemDto.getProductSizeId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품의 사이즈 정보를 찾을 수 없습니다."));

            // 상품 재고 확인 및 감소 처리
            if (productSize.getCnt() < itemDto.getQty()) {
                throw new CntNotEnoughException("재고가 부족합니다.");
            }
            productSize.setCnt(productSize.getCnt() - itemDto.getQty()); // 재고 감소

            // 주문 아이템 생성 및 추가
            int totalPrice = itemDto.getItemPrice() * itemDto.getQty(); // 개별 총 가격
            totalOrderPrice += totalPrice; // 주문 총 가격 합산

            OrderItem orderItem = orderMapper.toEntity(itemDto, product, productSize, order, totalPrice);
            order.getOrderItems().add(orderItem);
        }

        // 쿠폰 사용 처리 // 2025-04-08
        if (userCoupon != null) {
            userCoupon.useCoupon();
            userCouponRepository.save(userCoupon);
        }


        // 총 가격 및 주문 저장
        order.setTotalPrice(totalOrderPrice + requestDto.getShippingFee());
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);

    }

    // 장바구니에서 전체 구매하기
    @Transactional
    public OrderResponseDto createCartOrder(Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 유저입니다."));

        // 장바구니 조회 // 유저 하나당 하나의 장바구니를 가지기 때문에 user 삽입
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니입니다."));

        // 장바구니 비어있는지 확인
        if (cart.getCartItems().isEmpty()) {
            throw new CartItemEmpty("장바구니가 비어있습니다.");
        }

        // 장바구니에 담긴 CartItems들을 order orderItems로 변환 시작
        List<OrderItemRequestDto> orderItems = cart.getCartItems().stream()
                .map(cartItem -> new OrderItemRequestDto(
                        cartItem.getProduct().getProductId(),
                        cartItem.getProductSize().getProductSizeId(),
                        cartItem.getQty(),
                        cartItem.getItemPrice()
                ))
                .collect(Collectors.toList());

        // 주문요청  DTO 생성
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setOrderItems(orderItems);
        orderRequestDto.setShippingFee(cart.getShippingFee());


        // 기존 주문 생성 로직 재사용
        OrderResponseDto orderResponseDto = createOrder(userId, orderRequestDto);

        // 주문 완료 후 장바구니 비우기
        cart.getCartItems().clear();
        cartRepository.save(cart);


        return orderResponseDto;


    }

    // 장바구니에서 부분 구매하기 // 쿠폰 적용하게 되면 사용한 쿠폰은 True로 처리 됨
    // Cart 엔티티에서 userCouponId가 지속적으로 변경이 되어야한다
    @Transactional
    public OrderResponseDto createCartSelectOrder(Long userId
            , List<Long> selectCartItemIds
            , OrderRequestDto requestDto
           ) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 유저입니다."));

        // 유저는 하나의 장바구니만 가지고 있다.
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니입니다."));

        // 사용자가 선택한 CartItem만 필터링 됨
        List<CartItem> selectedCartItems = cart.getCartItems().stream()
                .filter(cartItem  -> selectCartItemIds.contains(cartItem.getCartItemId()))
                .collect(Collectors.toList());

        // 선택 된 상품이 없을 때
        if (selectedCartItems.isEmpty()) {
            throw new SelectedCartItemsNotFoundException("선택된 상품이 없습니다.");
        }

        // 쿠폰 유효성 검사 시작 //2025-04-09
        UserCoupon userCoupon = null;
        if (requestDto.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findById(requestDto.getUserCouponId())
                    .orElseThrow(() -> new UserCouponNotFoundException("쿠폰을 찾을 수 없습니다."));

            if (!userCoupon.getUser().getUserId().equals(userId)) {
                throw new CouponNotApplicableException("해당 쿠폰은 사용자 쿠폰이 아닙니다.");
            }

            if (userCoupon.getIsUsed()) {
                throw new CouponNotApplicableException("이미 사용한 쿠폰입니다.");
            }
        }

        // 장바구니에 담긴 CartItems들을 order orderItems로 변환 시작
        List<OrderItemRequestDto> orderItems = selectedCartItems.stream()
                .map(cartItem -> new OrderItemRequestDto(
                        cartItem.getProduct().getProductId(),
                        cartItem.getProductSize().getProductSizeId(),
                        cartItem.getQty(),
                        cartItem.getItemPrice()
                ))
                .collect(Collectors.toList());

        // 주문 요청 DTO 구성
        requestDto.setOrderItems(orderItems);
        requestDto.setShippingFee(cart.getShippingFee());
        requestDto.setUserId(userId);

        // 주문 생성
        OrderResponseDto orderResponseDto = createOrder(userId, requestDto);

        // 장바구니에서 해당 아이템 삭제
        cart.getCartItems().removeAll(selectedCartItems);
        cartRepository.save(cart);

        return orderResponseDto;
    }


    // 특정 유저가 구매한 모든 품목 (사이즈, 개수)까지 나와야 함
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrderByUser(Long userId) {
        // 특정 유저가 구매한것들
        List<Order> orders = orderRepository.findByUser_UserId(userId);

        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }


}
