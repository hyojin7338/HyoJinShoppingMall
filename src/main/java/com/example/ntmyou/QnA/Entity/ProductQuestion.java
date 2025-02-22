package com.example.ntmyou.QnA.Entity;

import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.QnA.Entity.Enum.ProductQuestionType;
import com.example.ntmyou.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class ProductQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productQuestion_id", nullable = false)
    private Long productQuestionId;

    @Enumerated(EnumType.STRING) // Enum 사용
    @Column(nullable = false)
    private ProductQuestionType questionType; // 문의 유형 추가

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String contents;

    // 유저는 여러개의 문의를 남길 수 있다 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //문의 내용을 작성한 사용자

    // 상품은 여러개의 문의가 남길 수 있다 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 문의가 속한 상품

    @Column(nullable = false)
    private Boolean isAnswered = false; // 답변 여부 기본 false 으로 설정

    @Column(updatable = false)
    private LocalDateTime createdAt;  // 문의 작성 시간

    @Column
    private LocalDateTime updatedAt;  // 문의 수정 시간

    // 판매자 답변 (1:N 관계)
    @OneToMany(mappedBy = "productQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAnswer> answers = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();  // 자동으로 현재 시간 설정
    }

    @PreUpdate // 수정 시 자동으로 들어감
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    //답변이 달리면  isAnswered 값을 True로 변경하는 메서드
    public void markAsAnswered() {
        this.isAnswered = true;
    }

}
