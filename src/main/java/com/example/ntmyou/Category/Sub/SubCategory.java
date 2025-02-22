package com.example.ntmyou.Category.Sub;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subCategory_id", nullable = false)
    private Long subCategoryId;

    @Column(unique = true)
    private String code;

    @Column
    private String name;  // 소분류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "childCategory_id", nullable = false)
    @JsonIgnore
    private ChildCategory childCategory;

    // 상품 생성시 ID를 받을 생성자가 필요함
    public SubCategory(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

}
