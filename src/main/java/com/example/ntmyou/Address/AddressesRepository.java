package com.example.ntmyou.Address;


import com.example.ntmyou.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressesRepository extends JpaRepository<Addresses, Long> {
    List<Addresses>findByUser_UserId(Long userId);

    // 기본 배송지 여부
    List<Addresses> findByUserAndIsDefault(User user, boolean isDefault);
}