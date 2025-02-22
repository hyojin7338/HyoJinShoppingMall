package com.example.ntmyou.QnA.Mapper;

import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.QnA.DTO.ProductAnswerRequestDto;
import com.example.ntmyou.QnA.DTO.ProductAnswerResponseDto;
import com.example.ntmyou.QnA.Entity.ProductAnswer;
import com.example.ntmyou.QnA.Entity.ProductQuestion;

public class ProductAnswerMapper {
    // requestDto -> Entity
    public static ProductAnswer toEntity(ProductAnswerRequestDto answerRequestDto
            , ProductQuestion productQuestion
            , Master master) {
        return ProductAnswer.builder()
                .productQuestion(productQuestion)
                .master(master)
                .answerContents(answerRequestDto.getAnswerContents())
                .build();
    }

    // Entity -> ResponseDto
    public static ProductAnswerResponseDto toResponseDto(ProductAnswer productAnswer) {
     return ProductAnswerResponseDto.builder()
             .productAnswerId(productAnswer.getProductAnswerId())
             .productQuestionId(productAnswer.getProductQuestion().getProductQuestionId())
             .masterName(productAnswer.getMaster().getName())
             .answerContents(productAnswer.getAnswerContents())
             .build();
    }
}
