package com.example.ntmyou.Favorite;

import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.User.Entity.User;

public class FavoriteMapper {
    // requestDto >  Entity
    public static Favorite toEntity(FavoriteRequestDto requestDto
            , User user
            , Product product) {
        return Favorite.builder()
                .user(user)
                .product(product)
                .build();
    }

    // Entity > ResponseDto
    public static FavoriteResponseDto toResponseDto(Favorite favorite) {
        return FavoriteResponseDto.builder()
                .favoriteId(favorite.getFavoriteId())
                .userName(favorite.getUser().getName())
                .productName(favorite.getProduct().getName())
                .favoriteAt(favorite.getFavoriteAt())
                .build();
    }
}
