package com.example.ntmyou.Favorite;

import com.example.ntmyou.Exception.FavoriteExistsException;
import com.example.ntmyou.Exception.FavoriteNotFoundException;
import com.example.ntmyou.Exception.ProductNotFoundException;
import com.example.ntmyou.Exception.UserCodeNotFoundException;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.User.Entity.User;
import com.example.ntmyou.User.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(UserRepository userRepository,
                           ProductRepository productRepository,
                           FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite addFavorite(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원이니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        // 중복확인
        boolean alreadyLiked = favoriteRepository.existsByUserAndProduct(user, product);
        if (alreadyLiked) {
            throw new FavoriteExistsException("이미 찜한 상품입니다.");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .build();

        return favoriteRepository.save(favorite);
    }

    // 특정유저가 찜 목록 전체 조회하기
    @Transactional(readOnly = true)
    public List<Favorite> getFindFavorite(Long userId) {
        // 유저가 존재하는지 확인하고
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserCodeNotFoundException("존재하지 않는 회원이니다."));
        return favoriteRepository.findByUser(user);
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
}
