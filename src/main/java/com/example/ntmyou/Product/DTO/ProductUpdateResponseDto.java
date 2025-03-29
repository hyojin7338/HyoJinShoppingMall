package com.example.ntmyou.Product.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductUpdateResponseDto {
    private String name; // 상품명
    private String contents; // 상품 설명
    private Integer amount; // 가격
    // private Integer cnt;  // 현 재고

    // productSize 엔티티 추가
     private List<ProductSizeResponseDto> sizes;

    private String parentsCategoryName; // 대분류 이름
    private String childCategoryName;   // 중분류 이름
    private String subCategoryName;     // 소분류 이름
    private String masterName;          // 판매자 이름

    private String mainImg;  // 대표 이미지
    private List<String > imageUrls; // 서브 이미지
}
