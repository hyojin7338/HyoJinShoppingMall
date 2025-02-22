package com.example.ntmyou.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByCode(String code);
    Optional<Admin> findByName(String name);
}
