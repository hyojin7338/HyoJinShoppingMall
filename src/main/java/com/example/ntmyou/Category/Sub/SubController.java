package com.example.ntmyou.Category.Sub;

import com.example.ntmyou.Category.Child.ChildRequestDto;
import com.example.ntmyou.Category.Child.ChildResponseDto;
import com.example.ntmyou.Category.Child.ChildService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubController {
    private final SubService subService;

    @PostMapping("/master/category/sub/{childId}")
    public ResponseEntity<SubResponseDto> createSub(@PathVariable Long childId,
                                                      @RequestBody @Valid SubRequestDto subRequestDto) {
        SubResponseDto subResponseDto = subService.createSub(subRequestDto, childId);
        return ResponseEntity.status(HttpStatus.CREATED).body(subResponseDto);
    }

    @GetMapping("/master/category/sub/find")
    public ResponseEntity<List<SubCategory>> findAllSubCategory() {
        return ResponseEntity.ok(subService.getAllSubCategory());
    }
}
