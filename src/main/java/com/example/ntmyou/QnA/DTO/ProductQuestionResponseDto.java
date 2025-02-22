package com.example.ntmyou.QnA.DTO;

import com.example.ntmyou.QnA.Entity.Enum.ProductQuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProductQuestionResponseDto {
    private Long productQuestionId;
    private String userName; // 유저 닉네임
    private String productName; // 상품명
    private String title; // 타이틀
    private String contents; // 내용
    private ProductQuestionType questionType;
    private Boolean isAnswered; // 답변 여부 추가
}
