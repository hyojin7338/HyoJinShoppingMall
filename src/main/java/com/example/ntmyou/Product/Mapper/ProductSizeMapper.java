package com.example.ntmyou.Product.Mapper;

import com.example.ntmyou.Product.DTO.ProductSizeRequestDto;
import com.example.ntmyou.Product.DTO.ProductSizeResponseDto;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;

public class ProductSizeMapper {
    // 상품사이즈 수정 request -> Entity
    public static ProductSize toEntity(ProductSizeRequestDto requestDto, Product product) {
        return ProductSize.builder()
                .cnt(requestDto.getCnt())
                .size(requestDto.getSize())
                .product(product)
                .build();
    }

    // Entity -> Response
    public static ProductSizeResponseDto toResponseDto(ProductSize productSize) {

        return ProductSizeResponseDto.builder()
                .productSizeId(productSize.getProductSizeId())
                .cnt(productSize.getCnt() != null ? productSize.getCnt() : 0)
                .size(productSize.getSize() != null ? productSize.getSize() : "")
                .build();
    }
}
