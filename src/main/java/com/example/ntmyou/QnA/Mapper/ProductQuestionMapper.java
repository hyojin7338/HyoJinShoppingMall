package com.example.ntmyou.QnA.Mapper;

import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.QnA.DTO.ProductQuestionRequestDto;
import com.example.ntmyou.QnA.DTO.ProductQuestionResponseDto;
import com.example.ntmyou.QnA.Entity.ProductQuestion;
import com.example.ntmyou.User.Entity.User;


public class ProductQuestionMapper {

    public static ProductQuestion toEntity(ProductQuestionRequestDto requestDto
            , User user, Product product) {
        return ProductQuestion.builder()
                .questionType(requestDto.getQuestionType())
                .user(user)
                .product(product)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .isAnswered(false)
                .build();
    }

    public static ProductQuestionResponseDto toResponseDto(ProductQuestion productQuestion) {
        return ProductQuestionResponseDto.builder()
                .productQuestionId(productQuestion.getProductQuestionId())
                .userName(productQuestion.getUser().getName())
                .productName(productQuestion.getProduct().getName())
                .title(productQuestion.getTitle())
                .contents(productQuestion.getContents())
                .questionType(productQuestion.getQuestionType())
                .isAnswered(productQuestion.getIsAnswered())
                .build();
    }
}
