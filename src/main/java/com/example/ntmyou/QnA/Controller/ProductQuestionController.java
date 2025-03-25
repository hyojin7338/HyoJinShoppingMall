package com.example.ntmyou.QnA.Controller;

import com.example.ntmyou.QnA.DTO.ProductQuestionRequestDto;
import com.example.ntmyou.QnA.DTO.ProductQuestionResponseDto;
import com.example.ntmyou.QnA.Service.ProductQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductQuestionController {

    private final ProductQuestionService productQuestionService;

    // 문의 생성
    @PostMapping("/product/questionCreate")
    public ResponseEntity<ProductQuestionResponseDto> createQuestion(@RequestBody @Valid
                                                         ProductQuestionRequestDto productQuestionRequestDto)
    {
        ProductQuestionResponseDto responseDto = productQuestionService.createQuestion(productQuestionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 수정
    @PatchMapping("/product/questionUpdate/{productQuestionId}")
    public ResponseEntity<ProductQuestionResponseDto> updateQuestion(
            @PathVariable Long productQuestionId,
            @RequestBody @Valid ProductQuestionRequestDto requestDto) {

        ProductQuestionResponseDto updatedQuestion = productQuestionService.updateQuestion(productQuestionId, requestDto);
        return ResponseEntity.ok(updatedQuestion);
    }

    // 내가 작성한 문의 전체 조회
    @GetMapping("/product/productQuestion/{userId}")
    public ResponseEntity<List<ProductQuestionResponseDto>> getUserQuestions(@PathVariable Long userId) {
        List<ProductQuestionResponseDto> userQuestions = productQuestionService.getQuestionByUser(userId);
        return ResponseEntity.ok(userQuestions);
    }

    // 판매자가 올린 상품 중 달린 문의 내용 전체 조회
    @GetMapping("/product/product_MasterQuestion/{masterId}")
    public ResponseEntity<List<ProductQuestionResponseDto>> getMasterQuestions(@PathVariable Long masterId) {
        List<ProductQuestionResponseDto> masterQuestions = productQuestionService.getQuestionByMaster(masterId);
        return ResponseEntity.ok(masterQuestions);
    }

    // 판매자가 특정 문의 내용 들어가기
    @GetMapping("/master/question/{productQuestionId}")
    public ResponseEntity<ProductQuestionResponseDto> getQuestionId(@PathVariable Long productQuestionId) {
        ProductQuestionResponseDto responseDto = productQuestionService.getProductQuestionById(productQuestionId);
        return ResponseEntity.ok(responseDto);
    }
}
