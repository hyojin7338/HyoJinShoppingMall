package com.example.ntmyou.Category.Child;



import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ChildRequestDto {
    private String code;
    private String name;
    private Long parentsId; // 대분류
}
