package com.example.ntmyou.Master.Repository;

import com.example.ntmyou.Master.Entity.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterRepository extends JpaRepository<Master, Long> {
    Optional<Master> findByCode(String code);

    Optional<Master> findByName(String name);
    Optional<Master> findByBusinessNo(String businessNo);

}
