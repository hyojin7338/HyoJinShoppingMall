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


    // 3. 특정 유저가 이미 찜을 하였는지 확인
    // RequestParam을 작성한 이유 : 여러개 값을 전달 할 떄 유용하고, 필수가 아닌 값은 생략 할 수 있다.
    @GetMapping("/favorite/check")
    public ResponseEntity<Boolean> checkFavorite(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        boolean exists = favoriteService.isFavorite(userId, productId);
        return ResponseEntity.ok(exists);
    }

    // 4. 찜 삭제
    @DeleteMapping("/favorite/remove/{userId}/{productId}")
    public ResponseEntity<String> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok("찜이 삭제되었습니다.");
    }


    // 5. 찜목록 화면에서 장바구니 저장하기
    // 찜목록에서 장바구니로 넘겨도 찜목록은 그대류 유지
    @PostMapping("/favorite/add-to-cart/{userId}/{productId}")
    public ResponseEntity<String> addToCartFromFavorite(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        favoriteService.addToCartFromFavorite(userId, productId);
        return ResponseEntity.ok("장바구니에 추가되었습니다.");
    }
}
