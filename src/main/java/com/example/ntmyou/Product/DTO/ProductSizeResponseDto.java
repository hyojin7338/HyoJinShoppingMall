package com.example.ntmyou.Product.DTO;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeResponseDto {
    private String size;
    private Integer cnt;
}
