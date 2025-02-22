package com.example.ntmyou.Category.Child;

import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Parents.ParentsRepository;
import com.example.ntmyou.Exception.ParentsCategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildService {
    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;
    private final ChildMapper childMapper;


    @Transactional
    public ChildResponseDto createChild(ChildRequestDto dto, Long parentId) {
        ParentsCategory parentsCategory = parentsRepository.findById(parentId)
                .orElseThrow(() -> new ParentsCategoryNotFoundException("부모 카테고리를 찾을 수 없습니다."));

        ChildCategory entity = childMapper.toEntity(dto, parentsCategory);
        ChildCategory savedEntity = childRepository.save(entity);

        return childMapper.responseDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<ChildCategory> getAllChild() {
        return childRepository.findAllWithParents();
    }
}
