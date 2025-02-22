package com.example.ntmyou.Favorite;

import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id", nullable = false)
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 찜한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 찜한 상품

    @Column(nullable = false)
    private LocalDateTime favoriteAt; // 찜한 시간

    @PrePersist
    public void prePersist() {
        this.favoriteAt = LocalDateTime.now();
    }

}
