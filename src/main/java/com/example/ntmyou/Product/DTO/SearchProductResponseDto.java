package com.example.ntmyou.Product.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SearchProductResponseDto {
    private Long productId;
    private String name;
    private String mainImg;
    private Integer amount;
    private String businessName;
}
