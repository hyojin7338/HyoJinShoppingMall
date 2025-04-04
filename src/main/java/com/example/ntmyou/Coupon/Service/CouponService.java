package com.example.ntmyou.Coupon.Service;

import com.example.ntmyou.Cart.DTO.CartCheckoutResponseDto;
import com.example.ntmyou.Cart.DTO.CartItemCheckDto;
import com.example.ntmyou.Cart.Entity.Cart;
import com.example.ntmyou.Cart.Repository.CartRepository;
import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.Coupon.DTO.CouponRequestDto;
import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import com.example.ntmyou.Coupon.Mapper.CouponMapper;
import com.example.ntmyou.Coupon.Repository.CouponRepository;
import com.example.ntmyou.Exception.*;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Master.Repository.MasterRepository;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
import com.example.ntmyou.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final MasterRepository masterRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;

    private final UserCouponRepository userCouponRepository;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    // 쿠폰 생성하기
    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto requestDto) {
        // 쿠폰 발급처 확인 // 판매자가 null 이면 사이트에서 제공한 쿠폰임
        Master master = null;
        // IssuedBy를 조회하여 발급처가 판매자이면 아래Repository를 조회함 -> 없으면 에러발생
        if (requestDto.getIssuedBy() == CouponIssuer.MASTER) {
            master = masterRepository.findById(requestDto.getMasterId())
                    .orElseThrow(() -> new MasterNotFoundException("판매자가 존재하지 않습니다."));
        }

        //  쿠폰이 특정 상품에만 적용
        Product product = null;
        // 특정상품에만 적용이 되어야한다
        if (requestDto.getProductId() != null) {
            product = productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("상품이 존재하지 않습니다."));
        }

        Coupon coupon = CouponMapper.toEntity(requestDto, master, product);
        couponRepository.save(coupon);

        return CouponMapper.toResponseDto(coupon);
    }

    // 회원가입 시 자동으로 쿠폰 생성
    @Transactional
    public void createSignupCoupon() {
        // 기존에 같은 쿠폰이 있나 조회
        boolean alreadyCoupon = couponRepository.existsByName("신규회원 5% 할인 쿠폰");
        if (!alreadyCoupon) {
            Coupon coupon = Coupon.builder()
                    .name("신규회원 5% 할인 쿠폰")
                    .discountType(DiscountType.PERCENT)
                    .discountValue(5) // 5% 할인
                    .minOrderAmount(10000) // 1만원 이상 주문 시 사용 가능
                    .maxDiscountAmount(5000) // 최대 5000원 할인 가능
                    .issuedBy(CouponIssuer.SITE) // 사이트 발급
                    .startDay(LocalDateTime.now())
                    .endDay(LocalDateTime.now().plusMonths(3)) // 3개월 후 만료
                    .isUse(true)
                    .isAutoIssued(true) // 신규 가입자 자동 지급 쿠폰
                    .build();
            couponRepository.save(coupon);
        }
    }

    // 쿠폰 조회
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUser_UserId(userId);
        return userCoupons.stream()
                .map(userCoupon -> CouponMapper.toResponseDto(userCoupon.getCoupon()))
                .collect(Collectors.toList());
    }


    // 사용가능한 쿠폰 조회
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getAvailableCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findByUser_UserIdAndIsUsedFalse(userId);

        return userCoupons.stream()
                .map(CouponMapper::toResponseDto) // `UserCoupon` 정보를 유지
                .collect(Collectors.toList());
    }


    // Cart 페이지에서 CartCheckout페이지로 넘어온 데이터 조회
    @Transactional(readOnly = true)
    public CartCheckoutResponseDto getCartCheckoutData(Long userId) {
        //  유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 유저입니다."));

        // 장바구니 조회
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("존재하지 않는 장바구니입니다."));

        // 장바구니가 비어 있는지 체크하기
        if (cart.getCartItems().isEmpty()) {
            throw new CartItemEmpty("장바구니가 비어있습니다.");
        }

        // 장바구니 상품 목록 조회
        List<CartItemCheckDto> cartItems = cart.getCartItems().stream()
                .map(cartItem -> new CartItemCheckDto(
                        cartItem.getCartItemId(),
                        cartItem.getProduct().getProductId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProductSize().getSize(),
                        cartItem.getQty(),
                        cartItem.getItemPrice()
                ))
                .collect(Collectors.toList());

        // 사용 가능한 쿠폰 조회 (기존 서비스 메서드 활용)
        List<CouponResponseDto> availableCoupons = getAvailableCoupons(userId);

        // 장바구니 총 가격 계산 (상품 가격 * 수량 합산)
        int totalPrice = cart.getCartItems().stream()
                .mapToInt(cartItem -> cartItem.getItemPrice() * cartItem.getQty())
                .sum();

        // 할인전 가격 기준으로 10만원 이상이면 배송비 무료 적용
        int shippingFee = (totalPrice >= 100000) ? 0 : 3000;

        CartCheckoutResponseDto responseDto = new CartCheckoutResponseDto();
        // 유저정보 추가 //2025-04-04
        responseDto.setUserId(user.getUserId());
        responseDto.setName(user.getName());
        responseDto.setTel(user.getTel());
        responseDto.setAddress(user.getAddress());
        responseDto.setRegion(user.getRegion());

        responseDto.setCartId(cart.getCartId());
        responseDto.setCartItems(cartItems);
        responseDto.setTotalPrice(totalPrice);
        responseDto.setShippingFee(shippingFee);
        responseDto.setDiscountAmount(0); // 기본적으로 쿠폰 미적용 상태
        responseDto.setFinalPrice(totalPrice + shippingFee);
        responseDto.setAvailableCoupons(availableCoupons);

        return responseDto;

    }

}
