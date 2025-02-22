package com.example.ntmyou.Category.Parents;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PaResponseDto {
    private Long parentsId;
    private String code;
    private String name;
}
