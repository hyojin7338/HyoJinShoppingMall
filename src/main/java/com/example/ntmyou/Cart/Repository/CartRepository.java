package com.example.ntmyou.Cart.Repository;

import com.example.ntmyou.Cart.Entity.Cart;
import com.example.ntmyou.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_UserId(Long userId);
}
