package com.example.ntmyou.Category.Child;

import com.example.ntmyou.Category.Parents.PaResponseDto;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Parents.ParentsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChildMapper {

    private final ParentsMapper parentsMapper;

    public ChildCategory toEntity(ChildRequestDto dto, ParentsCategory parentsId) {
        return ChildCategory.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .parentsId(parentsId)
                .build();
    }

    public ChildResponseDto responseDto(ChildCategory entity) {
        return ChildResponseDto.builder()
                .childId(entity.getChildCategoryId())
                .code(entity.getCode())
                .name(entity.getName())
                .parentsId(parentsMapper.toResponseDto(entity.getParentsId())) // 부모 DTO 변환
                .build();
    }
}
