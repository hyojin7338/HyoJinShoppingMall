package com.example.ntmyou.Favorite;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 1. 상품 찜하기
    @PostMapping("/favorite/add")
    public ResponseEntity<FavoriteResponseDto> addFavorite(@RequestBody @Valid FavoriteRequestDto requestDto) {
        FavoriteResponseDto favorite = favoriteService.addFavorite(requestDto);
        return ResponseEntity.ok(favorite);
    }

    // 2. 특정유저 전체 조회
    @GetMapping("/favorite/find/{userId}")
    public ResponseEntity<List<FavoriteResponseDto>> getUserFavorites(@PathVariable Long userId) {
        List<FavoriteResponseDto> favorites = favoriteService.getFindFavorite(userId);
        return ResponseEntity.ok(favorites);
    }
    // 3. 찜 삭제
    @DeleteMapping("/favorite/remove")
    public ResponseEntity<String> removeFavorite(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok("찜이 삭제되었습니다.");
    }
}
