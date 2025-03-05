package com.example.ntmyou.Coupon.Service;

import com.example.ntmyou.Coupon.Entity.Coupon;
import com.example.ntmyou.Coupon.DTO.CouponRequestDto;
import com.example.ntmyou.Coupon.DTO.CouponResponseDto;
import com.example.ntmyou.Coupon.Enum.CouponIssuer;
import com.example.ntmyou.Coupon.Enum.DiscountType;
import com.example.ntmyou.Coupon.Mapper.CouponMapper;
import com.example.ntmyou.Coupon.Repository.CouponRepository;
import com.example.ntmyou.Exception.MasterNotFoundException;
import com.example.ntmyou.Exception.ProductNotFoundException;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Master.Repository.MasterRepository;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.User.Entity.UserCoupon;
import com.example.ntmyou.User.Repository.UserCouponRepository;
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

}
