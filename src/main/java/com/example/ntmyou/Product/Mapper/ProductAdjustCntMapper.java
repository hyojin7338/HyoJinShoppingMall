package com.example.ntmyou.Product.Mapper;

import com.example.ntmyou.Product.DTO.ProductAdjustCntRequestDto;
import com.example.ntmyou.Product.DTO.ProductAdjustCntResponseDto;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;

public class ProductAdjustCntMapper {
    public static ProductSize toEntity(ProductAdjustCntRequestDto requestDto
            , Product product) {
        return ProductSize.builder()
                .cnt(requestDto.getAdjustCnt())
                .product(product)
                .build();
    }
    public static ProductAdjustCntResponseDto toResponseDto(ProductSize productSize) {
        return ProductAdjustCntResponseDto.builder()
                .productSizeId(productSize.getProductSizeId())
                .adjustCnt(productSize.getCnt() != null ? productSize.getCnt() : 0)
                .currentCnt(productSize.getCnt())
                .build();
    }
}
