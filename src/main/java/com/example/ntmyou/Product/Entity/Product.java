package com.example.ntmyou.Product.Entity;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Sub.SubCategory;
import com.example.ntmyou.Master.Entity.Master;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_Id", nullable = false)
    private Long productId;    // 상품ID

    @Column(nullable = false, unique = true)
    private String code;  // 상품은 관리할 유니크코드

    @Column
    private String name; // 상품명

    @Column
    private String contents; // 상품 설명

    @Column
    private Integer amount; // 가격

    @Column
    private Integer cnt; // 현 재고

    @Column
    private LocalDateTime sDay; // 등록날짜

    @Column
    private LocalDateTime updateDay; // 수정날짜

    @PrePersist
    public void prePersist() {
        if (this.sDay == null) {
            this.sDay = LocalDateTime.now();
        }
    }


    // 카테고리  대분류
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentsCategory_id", nullable = false)
    private ParentsCategory parentsCategory;

    // 카테고리 중분류
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "childCategory_id", nullable = false)
    private ChildCategory childCategory;

    // 카테고리 소분류
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subCategory_id", nullable = false)
    private SubCategory subCategory;

    // 판매자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Master master; // 판매자 (1:N)


    @Column(length = 1000)
    private String mainImgUrl;  // 대표 이미지 URL

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", length = 1000)
    private List<String> imageUrls = new ArrayList<>();

}
