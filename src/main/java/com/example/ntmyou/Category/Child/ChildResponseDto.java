package com.example.ntmyou.Category.Child;

import com.example.ntmyou.Category.Parents.PaResponseDto;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ChildResponseDto {
    private Long childId;
    private String code;
    private String name;
    private PaResponseDto parentsId; // 대분류 응답 값
}
