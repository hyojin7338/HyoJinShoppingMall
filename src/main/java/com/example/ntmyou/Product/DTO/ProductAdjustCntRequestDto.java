package com.example.ntmyou.Product.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter 
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAdjustCntRequestDto {
    @JsonProperty("adjustCnt")
    private Integer adjustCnt;   // 조정할 수량 (증가 또는 감소)


}
