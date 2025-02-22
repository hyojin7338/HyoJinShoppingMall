package com.example.ntmyou.Category.Parents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ParentsService {
    private final ParentsRepository parentsRepository;
    private final ParentsMapper parentsMapper;

    // 카테고리 대분류 생성
    @Transactional
    public PaResponseDto createParents(PaRequestDto dto) {
        ParentsCategory entity = parentsMapper.toEntity(dto);
        ParentsCategory savedEntity = parentsRepository.save(entity);
        return parentsMapper.toResponseDto(savedEntity);
    }

    // 카테고리 대분류 전체 조회
    @Transactional
    public List<PaResponseDto> getAllParents() {
        return parentsRepository.findAll().stream()
                .map(ParentsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
