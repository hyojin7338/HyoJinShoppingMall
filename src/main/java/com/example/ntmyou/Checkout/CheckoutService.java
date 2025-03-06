package com.example.ntmyou.Checkout;

import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.Coupon.Mapper.CouponMapper;
import com.example.ntmyou.Exception.ProductNotFoundException;
import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
import com.example.ntmyou.User.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserCouponRepository userCouponRepository;

    public CheckoutService(UserRepository userRepository,
                           ProductRepository productRepository,
                           UserCouponRepository userCouponRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userCouponRepository = userCouponRepository;
    }

    @Transactional(readOnly = true)
    public CheckoutResponseDto getCheckoutInfo(Long userId, Long productId) {
        // 유저정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("유저를 찾을 수 없습니다."));
        // 상품 정보 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        // 판매자 정보 가지고 오기
        String businessName = product.getName() != null ? product.getMaster().getBusinessName() : "알 수 없음";

        // 재고 수량 가지고 오기
        int totalStock = product.getSizes().stream()
                .mapToInt(ProductSize::getCnt)
                .sum();

        // 사용 가능한 쿠폰 조회
        List<UserCoupon> userCoupons = userCouponRepository.findByUser_UserIdAndIsUsedFalse(userId);
        List<CouponResponseDto> availableCoupons = userCoupons.stream()
                .map(CouponMapper::toResponseDto)
                .collect(Collectors.toList());

        return CheckoutResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .tel(user.getTel())
                .address(user.getAddress())
                .region(user.getRegion())
                .productId(product.getProductId())
                .productName(product.getName())
                .contents(product.getContents())
                .amount(product.getAmount())
                .cnt(totalStock)
                .mainImgUrl(product.getMainImgUrl())
                .businessName(businessName)
                .availableCoupons(availableCoupons)
                .build();
    }
}
