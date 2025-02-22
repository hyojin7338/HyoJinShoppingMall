package com.example.ntmyou.Category.Parents;

import org.springframework.stereotype.Component;

@Component
public class ParentsMapper {
    public ParentsCategory toEntity(PaRequestDto dto) {
        return ParentsCategory.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .build();
    }

    public static PaResponseDto toResponseDto (ParentsCategory entity) {
        return PaResponseDto.builder()
                .parentsId(entity.getParentCategoryId())
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }
}
