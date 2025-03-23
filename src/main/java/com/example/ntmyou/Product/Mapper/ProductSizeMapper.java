package com.example.ntmyou.Product.Mapper;

import com.example.ntmyou.Product.DTO.ProductSizeRequestDto;
import com.example.ntmyou.Product.DTO.ProductSizeResponseDto;
import com.example.ntmyou.Product.Entity.ProductSize;

public class ProductSizeMapper {
    // 상품사이즈 수정 request -> Entity
    public static ProductSize toEntity(ProductSizeRequestDto requestDto) {
        return ProductSize.builder()
                .cnt(requestDto.getCnt())
                .size(requestDto.getSize())
                .build();
    }

    // Entity -> Response
    public static ProductSizeResponseDto toResponseDto(ProductSize productSize) {
        return ProductSizeResponseDto.builder()
                .productSizeId(productSize.getProductSizeId())
                .cnt(productSize.getCnt())
                .size(productSize.getSize())
                .build();
    }
}
