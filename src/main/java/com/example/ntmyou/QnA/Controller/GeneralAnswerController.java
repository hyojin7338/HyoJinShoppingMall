package com.example.ntmyou.QnA.Controller;

import com.example.ntmyou.QnA.DTO.GeneralAnswerRequestDto;
import com.example.ntmyou.QnA.DTO.GeneralAnswerResponseDto;
import com.example.ntmyou.QnA.Service.GeneralAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GeneralAnswerController {

    private final GeneralAnswerService generalAnswerService;

    @PostMapping("/general/answerCreate")
    public ResponseEntity<GeneralAnswerResponseDto> createAnswer(@RequestBody @Valid GeneralAnswerRequestDto requestDto) {
        GeneralAnswerResponseDto responseDto = generalAnswerService.createAnswer(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/general/answerCreate/{generalQuestionId}")
    public ResponseEntity<List<GeneralAnswerResponseDto>> getAnswers(@PathVariable Long generalQuestionId) {
        List<GeneralAnswerResponseDto> responseDto = generalAnswerService.getAnswerByQuestion(generalQuestionId);
        return ResponseEntity.ok(responseDto);
    }
}
