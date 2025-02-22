package com.example.ntmyou.Cart.Repository;

import com.example.ntmyou.Cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
