package com.example.ntmyou.Product.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProductSizeRequestDto {
    private String size; // "S", "M", "L",
    private Integer cnt; // 사이즈별 재고 수량
}
