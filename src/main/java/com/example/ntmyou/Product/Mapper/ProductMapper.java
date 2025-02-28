package com.example.ntmyou.Product.Mapper;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Sub.SubCategory;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Product.DTO.ProductRequestDto;
import com.example.ntmyou.Product.DTO.ProductResponseDto;
import com.example.ntmyou.Product.DTO.ProductSizeResponseDto;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    // 상품 생성 //  requestDto -> Entity
    public Product toEntity(ProductRequestDto productRequestDto, ParentsCategory parentsId, ChildCategory childId, SubCategory subId, Master masterId) {
        Product product = Product.builder()
                .code(productRequestDto.getCode())
                .name(productRequestDto.getName())
                .contents(productRequestDto.getContents())
                .amount(productRequestDto.getAmount())
                .parentsCategory(parentsId)
                .childCategory(childId)
                .subCategory(subId)
                .master(masterId)
                .build();

        // 사이즈 매핑 추가
        if (productRequestDto.getSizes() != null) {
            List<ProductSize> sizeList = productRequestDto.getSizes().stream()
                    .map(sizeDto -> ProductSize.builder()
                            .size(sizeDto.getSize())
                            .cnt(sizeDto.getCnt())
                            .product(product) // ✅ 사이즈가 속하는 상품 설정
                            .build())
                    .collect(Collectors.toList());
            product.setSizes(sizeList);
        }

        return product;
    }


    // 상품 응답 // Entity -> ResponseDto
    // 삼항 연산자 사용 ->  조건식 ? 참일 때 반환할 값 : 거짓일 때 반환할 값;
    public ProductResponseDto toResponseDto(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .code(product.getCode())
                .name(product.getName())
                .contents(product.getContents())
                .amount(product.getAmount())
                //.cnt(product.getCnt())
                .sizes(product.getSizes().stream().map(size -> new ProductSizeResponseDto(size.getSize(), size.getCnt())).collect(Collectors.toList()))
                .parentsCategoryName(product.getParentsCategory() != null ? product.getParentsCategory().getName() : null)
                .childCategoryName(product.getChildCategory() != null ? product.getChildCategory().getName() : null)
                .subCategoryName(product.getSubCategory() != null ? product.getSubCategory().getName() : null)
                .businessName(product.getMaster() != null ? product.getMaster().getBusinessName() : null)
                .mainImg(product.getMainImgUrl())
                .imageUrls(product.getImageUrls() != null ? product.getImageUrls() : new ArrayList<>()) // null 방지
                .build();
    }

}
