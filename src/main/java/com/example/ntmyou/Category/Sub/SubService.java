package com.example.ntmyou.Category.Sub;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.example.ntmyou.Category.Child.ChildRepository;
import com.example.ntmyou.Exception.ChildCategoryNotFoundException;
import com.example.ntmyou.Exception.ParentsCategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubService {
    private final SubRepository subRepository;
    private final ChildRepository childRepository;
    private final SubMapper subMapper;

    @Transactional
    public SubResponseDto createSub(SubRequestDto dto, Long childId) {
        ChildCategory childCategory = childRepository.findById(childId)
                .orElseThrow(() -> new ChildCategoryNotFoundException("중분류 카테고리를 찾을 수 없습니다."));

        SubCategory entity = subMapper.toEntity(dto, childCategory);
        SubCategory savedEntity = subRepository.save(entity);

        return subMapper.responseDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<SubCategory> getAllSubCategory() {
        return subRepository.findAllWithChild();
    }
}
