package com.example.ntmyou.Favorite;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class FavoriteResponseDto {
    private Long favoriteId;
    private String userName;
    private String productName;
    private LocalDateTime favoriteAt; // 찜한 시간
}
