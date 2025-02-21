package com.example.ntmyou.User.Entity;

import com.example.ntmyou.Config.Enum.Gender;
import com.example.ntmyou.Config.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 20, nullable = false, unique = true)
    private String code; // ID 코드

    @Column(nullable = false, unique = true)
    private String name; // 닉네임

    @Column(nullable = false)
    private String password; // 패스워드

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.MALE; // 기본 설정은 남자

    @Column(nullable = false)
    private Integer age; // 나이

    @Column
    private String zipCode; // 우편번호

    @Column
    private String address; // 주소

    @Column
    private String region;  // 상세주소

    @Column
    private LocalDateTime sDay; // 가입날짜

    @Column
    private String brithDay; // 생일

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // 기본 값 일반회원

    @Column
    private Boolean credit; // 정지된 계정 여부


    @Column(length = 1000)
    private String mainImgUrl;  // 대표 이미지 URL

    @ElementCollection
    @CollectionTable(name = "user_images", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "image_url", length = 1000)
    private List<String> imageUrls = new ArrayList<>();


    @Column(length = 13)
    private String tel; // 전화번호



    // 프로필 정보 부분 업데이트 메서드 // 기존 정보 데이터 유지와 롤백 방지
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

    // 가입날짜 자동으로 들어가게 하기
    // PrePersist -> Entity가 DB 저장전에 호출 되기 때문에 자동으로 저장 됨
    @PrePersist
    public void prePersist() {
        // 가입날짜
        if (this.sDay == null) {
            this.sDay = LocalDateTime.now();
        }

        if (this.gender == null) {
            this.gender = Gender.MALE;
        }

        if (this.role == null) {
            this.role = Role.USER;
        }

        if (this.credit == null) {
            this.credit = false;
        }

    }
}
