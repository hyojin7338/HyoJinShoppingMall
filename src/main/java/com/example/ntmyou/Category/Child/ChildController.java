package com.example.ntmyou.Category.Child;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    // 중분류 생성
    @PostMapping("/master/category/child/{parentId}")
    public ResponseEntity<ChildResponseDto> createChild(@PathVariable Long parentId
            ,@RequestBody @Valid ChildRequestDto childRequestDto)  {
        ChildResponseDto childResponseDto = childService.createChild(childRequestDto,parentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(childResponseDto);
    }

    // 중분류 전체 조회
    @GetMapping("/master/categoryChild/find/{parentId}")
    public ResponseEntity<List<ChildCategory>> getAllChildByParentId(@PathVariable Long parentId) {
        return ResponseEntity.ok(childService.getAllChildByParentId(parentId));
    }
}
