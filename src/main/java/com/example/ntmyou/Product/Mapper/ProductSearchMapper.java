package com.example.ntmyou.Product.Mapper;

import com.example.ntmyou.Product.DTO.SearchProductResponseDto;
import com.example.ntmyou.Product.Entity.Product;

public class ProductSearchMapper {
    public static SearchProductResponseDto toDto(Product product) {
        return SearchProductResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .mainImg(product.getMainImgUrl()) // <--- null 값이어도 가지고 와야 함
                .amount(product.getAmount()) // <--- null 값이어도 가지고 와야 함
                .businessName(product.getMaster() != null ? product.getMaster().getBusinessName() : null)
                .build();
    }
}
