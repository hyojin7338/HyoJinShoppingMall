package com.example.ntmyou.QnA.Controller;

import com.example.ntmyou.QnA.DTO.ProductAnswerRequestDto;
import com.example.ntmyou.QnA.DTO.ProductAnswerResponseDto;
import com.example.ntmyou.QnA.Service.ProductAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductAnswerController {
    private final ProductAnswerService productAnswerService;

    // 답글달기
    @PostMapping("/product/answerCreate")
    public ResponseEntity<ProductAnswerResponseDto> createAnswer(@RequestBody @Valid ProductAnswerRequestDto requestDto) {
        ProductAnswerResponseDto responseDto = productAnswerService.createAnswer(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    // 내가 단 답변 조회
    @GetMapping("/product/answerCreate/{productQuestionId}")
    public ResponseEntity<List<ProductAnswerResponseDto>> getAnswers(@PathVariable Long productQuestionId) {
        List<ProductAnswerResponseDto> answers = productAnswerService.getAnswersByQuestion(productQuestionId);
        return ResponseEntity.ok(answers);
    }
}
