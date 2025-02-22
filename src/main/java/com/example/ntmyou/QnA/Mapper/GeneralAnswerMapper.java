package com.example.ntmyou.QnA.Mapper;

import com.example.ntmyou.Admin.Admin;
import com.example.ntmyou.QnA.DTO.GeneralAnswerRequestDto;
import com.example.ntmyou.QnA.DTO.GeneralAnswerResponseDto;
import com.example.ntmyou.QnA.Entity.GeneralAnswer;
import com.example.ntmyou.QnA.Entity.GeneralQuestion;

public class GeneralAnswerMapper {
    // requestDto -> Entity
    public static GeneralAnswer toEntity(GeneralAnswerRequestDto requestDto
            , GeneralQuestion generalQuestion
            , Admin admin) {
        return GeneralAnswer.builder()
                .generalQuestion(generalQuestion)
                .admin(admin)
                .answerContents(requestDto.getAnswerContents())
                .build();
    }

    // Entity -> responseDto
    public static GeneralAnswerResponseDto toResponseDto(GeneralAnswer generalAnswer) {
        return GeneralAnswerResponseDto.builder()
                .generalAnswerId(generalAnswer.getGeneralAnswerId())
                .generalQuestionId(generalAnswer.getGeneralQuestion().getGeneralQuestionId())
                .adminName(generalAnswer.getAdmin().getName())
                .answerContents(generalAnswer.getAnswerContents())
                .build();
    }
}
