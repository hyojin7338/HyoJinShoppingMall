package com.example.ntmyou.Category.Child;

import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Sub.SubCategory;
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
public class ChildCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "childCategory_id", nullable = false)
    private Long childCategoryId;
    @Column(unique = true)
    private String code;

    @Column
    private String name; // 중분류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentsCategory_id", nullable = false)
    @JsonIgnore
    private ParentsCategory parentsId;


    // 소분류(SubCategory)와 1:N 관계
    @OneToMany(mappedBy = "childCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SubCategory> subCategoryId = new ArrayList<>();

    // 상품 생성 할때 Id 받는 생성자가 필요함
    public ChildCategory(Long childCategoryId) {
        this.childCategoryId = childCategoryId;
    }

}
