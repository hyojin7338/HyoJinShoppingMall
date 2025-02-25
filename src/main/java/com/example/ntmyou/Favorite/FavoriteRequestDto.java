package com.example.ntmyou.Favorite;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FavoriteRequestDto {
    private Long userId;
    private Long productId;
}
