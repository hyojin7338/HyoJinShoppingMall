package com.example.ntmyou.Master.Entity;

import com.example.ntmyou.Config.Enum.Role;
import com.example.ntmyou.Product.Entity.Product;
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
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id", nullable = false)
    private Long masterId;

    @Column(nullable = false, unique = true)
    private String code; // 최대 8자리 입력 가능한 유니크 코드

    @Column(nullable = false, unique = true, length = 100)
    private String name; // 닉네임

    @Column(nullable = false, unique = true, length = 100)
    private String businessName; //상호명

    @Column(nullable = false, unique = true)
    private String businessNo; //사업자번호

    @Column(nullable = false, length = 100)
    private String password; // 비밀번호

    @Column
    private LocalDateTime sDay; // 가입날짜

    @Column(length = 10)
    private String zipCode; // 우편번호

    @Column(length = 300)
    private String address; // 주소

    @Column(length = 300)
    private String region; // 상세주소

    @Column
    private Boolean credit; // 쇼핑몰 관리자 휴먼 계정 여부

    @Column(length = 13)
    private String tel;

    public void updateProfileInfo(String zipCode, String address, String region, String tel) {
        this.zipCode = zipCode;
        this.address = address;
        this.region = region;
        this.tel = tel;
    }

    // 메인 이미지 URL 설정 메서드
    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    // 이미지 리스트 업데이트 메서드
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);
    }

    // 상품 생성시 ID를 받아야하는 생성자가 필요함
    public Master (Long masterId) {
        this.masterId = masterId;
    }

    @Enumerated(EnumType.STRING)
    private Role role = Role.MASTER; // 기본 값 일반회원

    // 내가 등록한 상품을 볼 수 있어야 하니까 양방향성을 가지고 있어야한다
    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>(); // 내가 등록한 상품들

    @PrePersist
    public void prePersist() {
        if (this.sDay == null) {
            this.sDay = LocalDateTime.now();
        }

        if (this.role == null) {
            this.role = Role.MASTER;
        }

    }


    @Column(length = 1000)
    private String mainImgUrl;  // 대표 이미지 URL

    @ElementCollection
    @CollectionTable(name = "master_images", joinColumns = @JoinColumn(name = "master_id"))
    @Column(name = "image_url", length = 1000)
    private List<String> imageUrls = new ArrayList<>();

}
