package com.example.ntmyou.Product.DTO;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeResponseDto {
    private Long productSizeId;
    private String size;
    private Integer cnt;
}
