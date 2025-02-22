package com.example.ntmyou.Category.Sub;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.example.ntmyou.Category.Child.ChildMapper;
import com.example.ntmyou.Category.Child.ChildResponseDto;
import com.example.ntmyou.Category.Parents.PaResponseDto;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubMapper {

    private final ChildMapper childMapper;

    public SubCategory toEntity(SubRequestDto dto, ChildCategory childCategory) {
        return SubCategory.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .childCategory(childCategory)
                .build();
    }

    public SubResponseDto responseDto(SubCategory entity) {
        return SubResponseDto.builder()
                .subCategoryId(entity.getSubCategoryId())
                .code(entity.getCode())
                .name(entity.getName())
                .childCategory(childMapper.responseDto(entity.getChildCategory())) // 중분류 DTO 변환
                .build();
    }
}

