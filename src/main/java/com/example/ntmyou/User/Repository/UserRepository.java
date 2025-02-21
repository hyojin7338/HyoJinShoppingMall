package com.example.ntmyou.User.Repository;

import com.example.ntmyou.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 코드 조회
    Optional<User>findByCode(String code);

    // 사용자 닉네임 조회
    Optional<User>findByName(String name);

    boolean existsByCode(String code);

    boolean existsByName(String name);
}
