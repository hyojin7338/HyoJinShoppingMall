package com.example.ntmyou.QnA.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class GeneralAnswerResponseDto {
    private Long generalAnswerId; // 답변
    private Long generalQuestionId; // 문의
    private String adminName; // 운영자
    private String answerContents; // 답변 내용
    private LocalDateTime answeredAt; // 답변 시간
}
