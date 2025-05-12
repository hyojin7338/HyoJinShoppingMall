package com.example.ntmyou.Category.Sub;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubController {
    private final SubService subService;

    @PostMapping("/master/category/sub/{childId}")
    public ResponseEntity<SubResponseDto> createSub(@PathVariable Long childId,
                                                      @RequestBody @Valid SubRequestDto subRequestDto) {
        SubResponseDto subResponseDto = subService.createSub(subRequestDto, childId);
        return ResponseEntity.status(HttpStatus.CREATED).body(subResponseDto);
    }

    @GetMapping("/master/categorySub/find/{childId}")
    public ResponseEntity<List<SubCategory>> getAllSubByChildId(@PathVariable Long childId) {
        return ResponseEntity.ok(subService.getAllSubCategory(childId));
    }
}
