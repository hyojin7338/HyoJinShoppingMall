package com.example.ntmyou.Category.Sub;

import com.example.ntmyou.Category.Child.ChildResponseDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SubResponseDto {
    private Long subCategoryId;
    private String code;
    private String name;
    private ChildResponseDto childCategory; // 중분류 포함
}
