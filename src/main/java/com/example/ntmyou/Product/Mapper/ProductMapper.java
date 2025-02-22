package com.example.ntmyou.Product.Mapper;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Sub.SubCategory;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Product.DTO.ProductRequestDto;
import com.example.ntmyou.Product.DTO.ProductResponseDto;
import com.example.ntmyou.Product.Entity.Product;

import java.util.ArrayList;

public class ProductMapper {

    // 상품 생성 //  requestDto -> Entity
    public Product toEntity(ProductRequestDto productRequestDto,
                            ParentsCategory parentsId,
                            ChildCategory childId,
                            SubCategory subId,
                            Master masterId
                            )
    {
       return Product.builder()
               .code(productRequestDto.getCode())
               .name(productRequestDto.getName())
               .contents(productRequestDto.getContents())
               .amount(productRequestDto.getAmount())
               .cnt(productRequestDto.getCnt())
               .parentsCategory(parentsId)
               .childCategory(childId)
               .subCategory(subId)
               .master(masterId)
               .build();
    }

    // 상품 응답 // Entity -> ResponseDto
    // 삼항 연산자 사용 ->  조건식 ? 참일 때 반환할 값 : 거짓일 때 반환할 값;
    public ProductResponseDto toResponseDto(Product product) {
        return ProductResponseDto.builder()
                .code(product.getCode())
                .name(product.getName())
                .contents(product.getContents())
                .amount(product.getAmount())
                .cnt(product.getCnt())
                .parentsCategoryName(product.getParentsCategory() != null ? product.getParentsCategory().getName() : null)
                .childCategoryName(product.getChildCategory() != null ? product.getChildCategory().getName() : null)
                .subCategoryName(product.getSubCategory() != null ? product.getSubCategory().getName() : null)
                .masterName(product.getMaster() != null ? product.getMaster().getName() : null)
                .mainImg(product.getMainImgUrl())
                .imageUrls(product.getImageUrls() != null ? product.getImageUrls() : new ArrayList<>()) // null 방지
                .build();
    }

}
