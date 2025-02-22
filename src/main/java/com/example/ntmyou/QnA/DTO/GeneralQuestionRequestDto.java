package com.example.ntmyou.QnA.DTO;

import com.example.ntmyou.Config.Enum.Role;
import com.example.ntmyou.QnA.Entity.Enum.GeneralQuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeneralQuestionRequestDto {
    // 문의 유형
    @NotNull(message = "문의 유형을 선택해주세요!")
    private GeneralQuestionType questionType;

    // 일반 유저인지, 판매자인지 확인을 해야해
    @NotNull(message = "Role을 확인해주세요")
    private Role role;

    // 작성자
    private Long userId;
    // 작성자
    private Long masterId;
    // 타이틀
    @NotBlank(message = "타이틀을 입력해 주세요")
    @Size(min = 4, max = 50)
    private String title;
    // 내용
    @NotBlank(message = "내용을 입력해 주세요")
    @Size(min = 4, max = 999)
    private String contents;
}
