package com.example.ntmyou.Product.DTO;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAdjustCntResponseDto {
    private Long productSizeId;
    private Integer adjustCnt;   // 조정할 수량 (증가 또는 감소)
    private Integer currentCnt; // 조정 후 최종 재고 확인하기
}
