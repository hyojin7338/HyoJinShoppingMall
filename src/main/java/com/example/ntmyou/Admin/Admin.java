package com.example.ntmyou.Admin;

import com.example.ntmyou.Config.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(nullable = false, unique = true)
    private String code; // 아이디

    @Column(nullable = false, unique = true)
    private String name; // 운영자

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN; // 운영자

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = Role.ADMIN;
        }
    }
}
