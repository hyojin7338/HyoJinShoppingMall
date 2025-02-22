package com.example.ntmyou.QnA.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GeneralAnswerRequestDto {
    private Long generalQuestionId; // 문의 Id
    private Long adminId; // 운영자Id
    private String answerContents; // 문의 답변
}
