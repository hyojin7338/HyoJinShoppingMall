package com.example.ntmyou.QnA.Entity;

import com.example.ntmyou.Config.Enum.Role;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.QnA.Entity.Enum.GeneralQuestionType;
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
public class GeneralQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "generalQuestion_id", nullable = false)
    private Long generalQuestionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GeneralQuestionType questionType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Master master;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 운영자에게 문의는 일반회원 또는 판매자 둘 다 할 수 있다.

    @Column(nullable = false)
    private Boolean isAnswered = false; // 답변 여부 기본 false 으로 설정

    @Column(updatable = false)
    private LocalDateTime createdAt;  // 문의 작성 시간

    @Column
    private LocalDateTime updatedAt;  // 문의 수정 시간

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();;  // 자동으로 현재 시간 설정
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 운영자 답변 (1:N) 관계
    @OneToMany(mappedBy = "generalQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneralAnswer> answer = new ArrayList<>();

    public void markAsAnswered() {
        this.isAnswered = true;
    }


}
