package com.example.ntmyou.Category.Sub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SubRequestDto {
    private String code;
    private String name;
    private Long childCategoryId; // (중분류 ID)
}
