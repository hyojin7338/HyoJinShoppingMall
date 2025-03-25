package com.example.ntmyou.Product.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Setter
@Getter
public class ProductRequestDto {

    @NotBlank(message = "코드를 입력해 주세요.")
    @Size(min = 2, max = 10)
    private String code; // 상품을 관리할 코드

    @NotBlank(message = "상품명을 입력해 주세요.")
    @Size(min = 2, max = 50)
    private String name; // 상품명

    @NotBlank(message = "상품명을 입력해 주세요.")
    @Size(max = 500)
    private String contents; // 상품 설명

    private Integer amount; // 가격

    //private Integer cnt;  // 현 재고 productSize 추가 후 주석처리
    private List<ProductSizeRequestDto> sizes; // 여라 상품 등록

    private Long parentsCategoryId;

    private Long childCategoryId;

    private Long subCategoryId;

    private Long masterId;

    private String mainImg;  // 대표 이미지
    private List<String > imageUrls; // 서브 이미지
}
