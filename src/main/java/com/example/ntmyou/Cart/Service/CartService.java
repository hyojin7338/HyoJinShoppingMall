package com.example.ntmyou.Cart.Service;

import com.example.ntmyou.Cart.Entity.Cart;
import com.example.ntmyou.Cart.Entity.CartItem;
import com.example.ntmyou.Cart.Repository.CartRepository;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import com.example.ntmyou.Exception.*;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserCouponRepository userCouponRepository;
    private final ProductRepository productRepository;

    // 장바구니에 상품 추가하는 메서드
    @Transactional
    public void addProductToCart(Long cartId, Long productId, int qty){
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

        // 사용한 쿠폰인지 확인
        if (userCoupon.getIsUsed()) {
            throw new UserCouponAlreadyUsed("이미 사용한 쿠폰입니다.");
        }
        // 쿠폰 할인 금액 계산 하기
        int discountAmount = 0;
        if (userCoupon.getCoupon().getDiscountType() == DiscountType.PERCENT) {
            discountAmount = (cart.getTotalPrice() * userCoupon.getCoupon().getDiscountValue()) / 100;
            discountAmount = Math.min(discountAmount, userCoupon.getCoupon().getMaxDiscountAmount());
        } else {
            discountAmount = userCoupon.getCoupon().getDiscountValue(); // 정액할인
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
