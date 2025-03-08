package com.example.ntmyou.Address;

import com.example.ntmyou.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressesRepository extends JpaRepository<Addresses, Long> {
    List<Addresses>findByUser_UserId(Long userId);
}