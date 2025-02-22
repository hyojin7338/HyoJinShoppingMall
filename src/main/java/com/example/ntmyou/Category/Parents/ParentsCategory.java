package com.example.ntmyou.Category.Parents;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class ParentsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parents_id", nullable = false)
    private Long parentCategoryId;

    @Column(unique = true)
    private String code;

    @Column
    private String name;  // 대분류명

    // 중분류(ChildCategory)와 1:N 관계
    @OneToMany(mappedBy = "parentsId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChildCategory> childCategories = new ArrayList<>();

    // product 생성 할 때 ID를 받는 생성자가 있어야한다.
    public ParentsCategory(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
