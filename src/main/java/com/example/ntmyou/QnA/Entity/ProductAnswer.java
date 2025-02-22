package com.example.ntmyou.QnA.Entity;

import com.example.ntmyou.Master.Entity.Master;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ProductAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productAnswer_id", nullable = false)
    private Long productAnswerId;

    @Column(nullable = false, length = 1000)
    private String answerContents;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;  // 문의 작성 시간

    @Column
    private LocalDateTime updatedAt;  // 문의 수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productQuestion_id", nullable = false)
    private ProductQuestion productQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Master master;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.productQuestion.markAsAnswered(); // 답변이 등록될 때 isAnswered = true로 변경 추가
    }

    @PreUpdate // 수정 시 자동으로 들어감
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
