package com.example.ntmyou.QnA.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductAnswerRequestDto {
    private Long productQuestionId; // 문의Id
    private Long masterId;
    private String answerContents; // 문의 답변
}
