package com.example.ntmyou.QnA.Mapper;

import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.QnA.DTO.GeneralQuestionRequestDto;
import com.example.ntmyou.QnA.DTO.GeneralQuestionResponseDto;
import com.example.ntmyou.QnA.Entity.GeneralQuestion;
import com.example.ntmyou.User.Entity.User;

public class GeneralQuestionMapper {
    //  운영자에게 문의하기
    public static GeneralQuestion toEntity(GeneralQuestionRequestDto requestDto
    , User user, Master master) {
        return GeneralQuestion.builder()
                .questionType(requestDto.getQuestionType())
                .role(requestDto.getRole()) // 제거 해도 될듯 //2025-03-19
                .user(user)
                .master(master)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .isAnswered(false)
                .build();
    }
    // Entity -> ResponseDto
    public static GeneralQuestionResponseDto toResponseDto(GeneralQuestion generalQuestion) {
        return GeneralQuestionResponseDto.builder()
                .generalQuestionId(generalQuestion.getGeneralQuestionId())
                .userName(generalQuestion.getUser() != null ? generalQuestion.getUser().getName() : "일반회원")
                .masterName(generalQuestion.getMaster() != null ? generalQuestion.getMaster().getName() : "판매자")
                .title(generalQuestion.getTitle())
                .contents(generalQuestion.getContents())
                .questionType(generalQuestion.getQuestionType())
                .role(generalQuestion.getRole())
                .isAnswered(generalQuestion.getIsAnswered())
                .build();
    }


}
