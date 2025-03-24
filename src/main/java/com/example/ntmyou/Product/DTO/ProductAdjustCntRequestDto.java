package com.example.ntmyou.Product.DTO;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAdjustCntRequestDto {
    private Integer adjustCnt;   // 조정할 수량 (증가 또는 감소)


}
