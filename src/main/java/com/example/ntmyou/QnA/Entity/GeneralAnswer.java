package com.example.ntmyou.QnA.Entity;

import com.example.ntmyou.Admin.Admin;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class GeneralAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "generalAnswer_id", nullable = false)
    private Long generalAnswerId;

    @Column(nullable = false, length = 1000)
    private String answerContents;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;  // 문의 작성 시간

    @Column
    private LocalDateTime updatedAt;  // 문의 수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generalQuestion_id", nullable = false)
    private GeneralQuestion generalQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.generalQuestion.markAsAnswered();
    }

    @PreUpdate // 수정 시 자동으로 들어감
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
