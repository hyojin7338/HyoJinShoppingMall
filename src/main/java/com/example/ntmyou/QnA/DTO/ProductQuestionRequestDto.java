package com.example.ntmyou.QnA.DTO;

import com.example.ntmyou.QnA.Entity.Enum.ProductQuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProductQuestionRequestDto {
    @NotNull(message = "문의 유형을 선택해주세요.")
    private ProductQuestionType questionType; // 문의 유형 추가
    // 작성자
    private Long userId;
    // 상품
    private Long productId;

    // 타이틀
    @NotBlank(message = "타이틀을 입력해 주세요")
    @Size(min = 4, max = 50)
    private String title;

    // 내용
    @NotBlank(message = "내용을 입력해 주세요")
    @Size(min = 4, max = 999)
    private String contents;
}
