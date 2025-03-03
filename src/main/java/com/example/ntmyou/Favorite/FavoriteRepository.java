package com.example.ntmyou.Favorite;

import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite>findByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);

    Optional<Favorite> findByUserAndProduct(User user, Product product);

    // 특정 유저가 특정 상품을 찜한 데이터를 가져오는 메서드
    Optional<Favorite> findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
}