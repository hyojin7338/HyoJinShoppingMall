package com.example.ntmyou.Product.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductUpdateRequestDto {
    // 상품 수정은 관리코드를 제외하고 모두 변경이 가능하다
    private String name; // 상품명
    private String contents; // 상품 설명
    private Integer amount; // 가격
    private Integer cnt;  // 현 재고

    private Long parentsCategoryId; // 대분류 ID
    private Long childCategoryId;   // 중분류 ID
    private Long subCategoryId;     // 소분류 ID
    private Long masterId;          // 판매자 ID

    private String mainImg;  // 대표 이미지
    private List<String > imageUrls; // 서브 이미지
}
