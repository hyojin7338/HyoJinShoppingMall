package com.example.ntmyou.Favorite;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 1. 상품 찜하기
    @PostMapping("/favorite/add/{userId}/{productId}")
    public ResponseEntity<String> addFavorite(@PathVariable Long userId
            , @PathVariable Long productId) {
        favoriteService.addFavorite(userId, productId);
        return ResponseEntity.ok("상품이 찜 목록에 추가되었습니다.");
    }

    // 2. 특정유저 전체 조회
    @GetMapping("/favorite/find/{userId}")
    public ResponseEntity<List<Favorite>> findFavorite(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.getFindFavorite(userId);
        return ResponseEntity.ok(favorites);
    }

    // 3. 찜 삭제
    @DeleteMapping("/favorite/delete/{userId}/{productId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long userId
            , @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok("상품이 찜 목록에서 삭제되었습니다.");
    }
}
