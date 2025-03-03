package com.example.ntmyou.Favorite;

import com.example.ntmyou.Cart.Entity.Cart;
import com.example.ntmyou.Cart.Entity.CartItem;
import com.example.ntmyou.Cart.Repository.CartRepository;
import com.example.ntmyou.Exception.*;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    private final CartRepository cartRepository;

    public FavoriteService(UserRepository userRepository,
                           ProductRepository productRepository,
                           FavoriteRepository favoriteRepository,
                           CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.favoriteRepository = favoriteRepository;
        this.cartRepository = cartRepository;
    }

    // RequestDto, ResponseDto, Mapper 추가하여 로직 변경 // 2025-02-25
    @Transactional
    public FavoriteResponseDto addFavorite(FavoriteRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원이니다."));

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        // requestDto -> Mapper 저장
        Favorite favorite = FavoriteMapper.toEntity(requestDto, user, product);

        // 서비스 저장
        favorite = favoriteRepository.save(favorite);

        return FavoriteMapper.toResponseDto(favorite);
    }


    // 특정유저가 찜 목록 전체 조회하기
    // RequestDto, ResponseDto, Mapper 추가하여 로직 변경 // 2025-02-25
    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getFindFavorite(Long userId) {
        // 유저가 존재하는지 확인하고
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원이니다."));
        // 찜한 목록 조회 후 DTO 변환
        List<Favorite> favorites = favoriteRepository.findByUser(user);

        return favorites.stream()
                .map(FavoriteMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // 특정유저가 이미 찜한 상태인지 확인하기
    @Transactional(readOnly = true)
    public boolean isFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 유저입니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        return favoriteRepository.existsByUserAndProduct(user, product);
    }

    // 특정 찜한 상품 삭제
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원입니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new FavoriteNotFoundException("찜한 상품이 없습니다."));

        favoriteRepository.delete(favorite);
    }

    // 5. 찜목록 화면에서 장바구니 저장하기
    // 찜목록에서 장바구니로 넘겨도 찜목록은 그대류 유지
    @Transactional
    public void addToCartFromFavorite(Long userId, Long productId) {
        // 유저의 장바구니 찾기
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CartNotFoundException("장바구니가 존재하지 않습니다."));
        // 찜한 상품이 존재하는지 확인
        Favorite favorite = favoriteRepository.findByUser_UserIdAndProduct_ProductId(userId,productId)
                .orElseThrow(() -> new FavoriteNotFoundException("찜한 상품이 아닙니다."));
        // 상품 정보 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("전재하지 않는 상품입니다."));
        // 장바구니에 추가하는 기존 로직 재사용
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .qty(1) // 기본 수량 1개
                    .itemPrice(product.getAmount())
                    .discountAmount(0)
                    .totalPrice(product.getAmount())
                    .build();
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.updateQuantity(cartItem.getQty() + 1);
        }
        // 장바구니 가격 업데이트
        cart.updateCartTotals();
        cartRepository.save(cart);
    }
}
