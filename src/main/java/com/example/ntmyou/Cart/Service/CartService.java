package com.example.ntmyou.Cart.Service;

import com.example.ntmyou.Cart.DTO.CartItemDto;
import com.example.ntmyou.Cart.DTO.CartResponseDto;
import com.example.ntmyou.Cart.Entity.Cart;
import com.example.ntmyou.Cart.Entity.CartItem;
import com.example.ntmyou.Cart.Repository.CartRepository;
import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import com.example.ntmyou.Exception.*;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserCouponRepository userCouponRepository;
    private final ProductRepository productRepository;

    // 장바구니에 상품 추가하는 메서드
    @Transactional
    public void addProductToCart(Long cartId, Long productId, int qty) {
        // 장바구니 조회
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니입니다."));

        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        // qty 검증 (0 이하이면 예외 발생)
        if (qty <= 0) {
            throw new QtyZeroException("수량은 1개 이상이여야합니다.");
        }

        // 장바구니에 같은 상품(productId)이 있는지 조회
        // 해당 상품이 있으면 CartItem을 가져오고 없으면 null을 반환한다.
        // 코드분석 Stream API 활용하여 Stream 객체로 변환
        CartItem cartItem = cart.getCartItems().stream()
                // .filter는 조건에 맞는 요소만 추려내는 것 // 람다 표현식으로 작성 함
                .filter(item -> item.getProduct().getProductId().equals(productId))
                // 첫 번째로 일치하는 요소를 찾는 메서드
                .findFirst()
                // 값이 있으면 해당 cartItem 반환하고 없으면 null을 반환
                .orElse(null);


        if (cartItem == null) {
            // 새 상품 추가
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .qty(qty)
                    .itemPrice(product.getAmount()) // 상품 가격 저장
                    .discountAmount(0) // 기본 할인은 없음
                    .totalPrice(product.getAmount() * qty)
                    .build();
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.updateQuantity(cartItem.getQty() + qty);
        }

        // 장바구니 총 가격 업데이트 후 저장
        cart.updateCartTotals();
    }

    // 장바구니 총 가격 및 배송비 업데이트
    @Transactional
    public void updateCartTotals(Long cartId) {
        // 장바구니 조회
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니입니다."));

        // 총 가격 계산 --> 할인 전
        int totalPrice = cart.getCartItems().stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();

        // 기존 할인 금액 초기화
        int discountAmount = cart.getCartItems().stream()
                .mapToInt(CartItem::getDiscountAmount)
                .sum();
        // 장바구니에 쿠폰이 적용된 경우 추가 할인 계산
        if (cart.getAppliedCoupon() != null) {
            UserCoupon userCoupon = cart.getAppliedCoupon();
            Coupon coupon = userCoupon.getCoupon();
            int additionalDiscount = 0;

            //  할인 적용 가능 여부 체크 (최소 주문 금액 조건 충족 여부)
            if (totalPrice >= coupon.getMinOrderAmount()) {
                if (coupon.getDiscountType() == DiscountType.PERCENT) {
                    //  퍼센트 할인: 최대 할인 금액(`maxDiscountAmount`) 고려하여 할인 적용
                    additionalDiscount = (totalPrice * coupon.getDiscountValue()) / 100;
                    additionalDiscount = Math.min(additionalDiscount, coupon.getMaxDiscountAmount()); // 최대 할인 제한 적용
                } else if (coupon.getDiscountType() == DiscountType.FIXED) {
                    //  정액 할인: 정해진 할인 금액 적용
                    additionalDiscount = coupon.getDiscountValue();
                }

                // 적용된 할인 금액 업데이트
                discountAmount += additionalDiscount;
            } else {
                System.out.println("⚠️ 최소 주문 금액 미달로 쿠폰 적용 불가");
            }
        }
        // 할인전 가격 기준으로 10만원 이상이면 배송비 무료 적용
        int shippingFee = (totalPrice >= 100000) ? 0 : 3000;

        // 최종 결제 금액 계산
        int finalPrice = totalPrice - discountAmount + shippingFee;

        System.out.println("✅ 장바구니 총 가격 (할인 전): " + totalPrice);
        System.out.println("✅ 장바구니 할인 금액: " + discountAmount);
        System.out.println("✅ 적용된 배송비: " + shippingFee);
        System.out.println("✅ 최종 결제 금액: " + finalPrice);


        // 장바구니 업데이트
        cart.setTotalPrice(totalPrice);
        cart.setDiscountAmount(discountAmount);
        cart.setShippingFee(shippingFee);
        cart.setFinalPrice(finalPrice);


        // 변경사항 저장
        cartRepository.save(cart);

    }

    // 특정 유저 조회
    @Transactional(readOnly = true)
    public CartResponseDto getCartByUserId(Long userId) {
        // 특정 유저의 장바구니 찾기
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지 않습니다."));

        updateCartTotals(cart.getCartId());

        // 장바구니에 담긴 상품들을 DTO로 변환
        List<CartItemDto> cartItems = cart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        item.getCartItemId(),
                        item.getProduct().getProductId(),
                        item.getProduct().getName(),
                        item.getProduct().getAmount(),
                        item.getQty()
                ))
                .collect(Collectors.toList());

        return new CartResponseDto(cart.getCartId(), cartItems, cart.getTotalPrice(), cart.getShippingFee(), cart.getFinalPrice());
    }

    // 장바구니 삭제
    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지 않습니다."));

        // 장바구니에서 해당 상품 제거
        cart.getCartItems().removeIf(item -> item.getProduct().getProductId().equals(productId));

        // 배송비 및 최정 결제 금액 재 업데이트
        updateCartTotals(cartId);
    }

    // 쿠폰 취소
    @Transactional
    public void removeCouponFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지 않습니다."));

        //  기존 쿠폰 제거
        cart.setAppliedCoupon(null);

        //  장바구니 총 가격 업데이트
        updateCartTotals(cartId);

        //  변경 사항 저장
        cartRepository.save(cart);
    }

    // 쿠폰적용하기
    @Transactional
    public void applyCouponToCart(Long cartId, Long userCouponId) {
        // 장바구니 조회
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지 않습니다."));

        // 사용자 쿠폰 조회
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new UserCouponNotFoundException("사용자 쿠폰은 존재하지 않습니다."));

        // 쿠폰이 적용된 경우 먼저 적용한 쿠폰을 취소를 해야한다  // 중복 방지
        if (cart.getAppliedCoupon() != null) {
            throw new CouponAlreadyAppliedException("이미 쿠폰이 적용되었습니다. 먼저 기존 쿠폰을 취소하세요.");
        }

        //  쿠폰 검증 // 사용한 쿠폰인지 확인
        if (userCoupon.getIsUsed()) {
            throw new UserCouponAlreadyUsed("이미 사용한 쿠폰입니다.");
        }

        //  쿠폰이 SITE or 특정 판매자 쿠폰인지 검증
        if (userCoupon.getCoupon().getIssuedBy() == CouponIssuer.MASTER) {
            boolean isApplicable = cart.getCartItems().stream()
                    .anyMatch(item -> item.getProduct().getMaster().equals(userCoupon.getCoupon().getMaster()));

            if (!isApplicable) {
                throw new CouponNotApplicableException("이 쿠폰은 해당 상품에 적용할 수 없습니다.");
            }
        }

        // 장바구니에 쿠폰 적용
        cart.setAppliedCoupon(userCoupon);

        cart.updateCartTotals();
        // 저장
        cartRepository.save(cart);
    }

    // 결제 후 쿠폰 사용 처리
    @Transactional
    public void checkOut(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지 않습니다."));

        // 쿠폰이 적용되어 있으면 사용처리
        if (cart.getAppliedCoupon() != null) {
            // isUsed = true 로변경하여 쿠폰이 다시 사용되지 않도록 막아야한다
            cart.getAppliedCoupon().useCoupon();
            userCouponRepository.save(cart.getAppliedCoupon());
        }

        // 장바구니 상태 변경
        cart.setIsCheckedOut(true);
        cartRepository.save(cart);
    }
}
