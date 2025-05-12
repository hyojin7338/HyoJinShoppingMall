package com.example.ntmyou.Category.Parents;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParentsController {

    private final ParentsService parentsService;

    // 대분류 생성
    @PostMapping("/master/category/parents")
    public ResponseEntity<PaResponseDto> createParent(@RequestBody PaRequestDto dto) {
        PaResponseDto response = parentsService.createParents(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 대분류 전체 조회
    @GetMapping("/master/category/parents/find")
    public ResponseEntity<List<PaResponseDto>> getAllParents() {
        return ResponseEntity.ok(parentsService.getAllParents());
    }
}
