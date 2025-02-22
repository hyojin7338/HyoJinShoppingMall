package com.example.ntmyou.Category.Child;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChildRepository extends JpaRepository<ChildCategory, Long> {
    @Query("SELECT c FROM ChildCategory c JOIN FETCH c.parentsId")
    List<ChildCategory> findAllWithParents();
}
