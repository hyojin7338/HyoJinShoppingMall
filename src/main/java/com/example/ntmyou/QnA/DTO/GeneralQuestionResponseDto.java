package com.example.ntmyou.QnA.DTO;

import com.example.ntmyou.Config.Enum.Role;
import com.example.ntmyou.QnA.Entity.Enum.GeneralQuestionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GeneralQuestionResponseDto {
    private Long generalQuestionId;
    private String userName; // 유저닉네임
    private String masterName; // 판매자닉네임
    private String title; // 타이틀
    private String contents; // 내용
    private GeneralQuestionType questionType; // 문의유형
    private Role role;
    private Boolean isAnswered; // 답변 여부
}
