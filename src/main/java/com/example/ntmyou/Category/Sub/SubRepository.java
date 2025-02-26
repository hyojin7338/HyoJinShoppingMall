package com.example.ntmyou.Category.Sub;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubRepository extends JpaRepository<SubCategory, Long> {
    @Query("select s From SubCategory s WHERE s.childCategory.childCategoryId = :childId")
    List<SubCategory> findAllWithChild(@Param("childId") Long childId);

}
