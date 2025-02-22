package com.example.ntmyou.Category.Sub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubRepository extends JpaRepository<SubCategory, Long> {
    @Query("select s From SubCategory s JOIN FETCH s.childCategory")
    List<SubCategory> findAllWithChild();

}
