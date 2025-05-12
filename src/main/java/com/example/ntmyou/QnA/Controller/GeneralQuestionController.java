package com.example.ntmyou.QnA.Controller;

import com.example.ntmyou.QnA.DTO.GeneralQuestionRequestDto;
import com.example.ntmyou.QnA.DTO.GeneralQuestionResponseDto;
import com.example.ntmyou.QnA.Service.GeneralQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GeneralQuestionController {

    // 운영자에게 문의하기
    private final GeneralQuestionService generalQuestionService;

    @PostMapping("/general/question")
    public ResponseEntity<GeneralQuestionResponseDto> createQuestion
            (@RequestBody @Valid GeneralQuestionRequestDto requestDto) {
        GeneralQuestionResponseDto responseDto = generalQuestionService.createQuestion(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 운영자에게 문의하기 수정
    @PatchMapping("/general/question/{generalQuestionId}")
    public ResponseEntity<GeneralQuestionResponseDto> updateQuestion(@PathVariable Long generalQuestionId,
                                                                     @RequestBody @Valid GeneralQuestionRequestDto requestDto) {
        GeneralQuestionResponseDto responseDto = generalQuestionService.updateQuestion(generalQuestionId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 내가 작성한 문의 전체 조회(USER)
    @GetMapping("/general/userQuestion/{userId}")
    public ResponseEntity<List<GeneralQuestionResponseDto>> updateQuestionByUser(@PathVariable Long userId) {
        List<GeneralQuestionResponseDto> responseDto = generalQuestionService.getQuestionByUser(userId);
        return ResponseEntity.ok(responseDto);
    }

    // 내가 작성한 문의 전체 조회(Master)
    @GetMapping("/general/masterQuestion/{masterId}")
    public ResponseEntity<List<GeneralQuestionResponseDto>> updateQuestionByMaster(@PathVariable Long masterId) {
        List<GeneralQuestionResponseDto> responseDto = generalQuestionService.getQuestionByMaster(masterId);
        return ResponseEntity.ok(responseDto);
    }

}
