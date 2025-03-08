package com.example.ntmyou.Address;

import com.example.ntmyou.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long addressId; // 주문확인(checkout)에서 배송지 변경시 사용

    private String address; // 주소
    private String region; // 상세주소
    private String receiverName; // 받는사람
    private String receiverTel; // 받는사람 전화번호

    private boolean isDefault;  // 기본 배송지 여부

    @ManyToOne // 유저는 많은 배송정보를 가질 수 있다.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
