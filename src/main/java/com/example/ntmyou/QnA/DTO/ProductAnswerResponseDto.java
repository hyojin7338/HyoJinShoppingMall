package com.example.ntmyou.QnA.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ProductAnswerResponseDto {
    private Long productAnswerId; // 답변
    private Long productQuestionId; // 문의
    private String masterName; // 마스터
    private String answerContents; // 답변 내용
    private LocalDateTime answeredAt; // 답변 시간
}
