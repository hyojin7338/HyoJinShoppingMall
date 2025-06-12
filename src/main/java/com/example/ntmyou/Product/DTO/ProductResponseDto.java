package com.example.ntmyou.Product.DTO;



import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductResponseDto {
    private Long productId;
    private String code; // 상품을 관리할 코드
    private String name; // 상품명
    private String contents; // 상품 설명
    private Integer amount; // 가격
   // private Integer cnt;  // 현 재고 // productSize 엔티티 추가 후 주석처리
    private List<ProductSizeResponseDto> sizes;

    private String parentsCategoryName; // 대분류 이름
    private String childCategoryName;   // 중분류 이름
    private String subCategoryName;     // 소분류 이름
    //private String masterName;          // 판매자 이름
    private String businessName; // 상호명 추가 함

    private String mainImg;  // 대표 이미지
    private List<String > imageUrls; // 서브 이미지

}
