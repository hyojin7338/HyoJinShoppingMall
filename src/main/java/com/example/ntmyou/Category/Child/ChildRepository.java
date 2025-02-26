package com.example.ntmyou.Category.Child;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildRepository extends JpaRepository<ChildCategory, Long> {
    @Query("SELECT c FROM ChildCategory c WHERE c.parentsId.parentCategoryId = :parentId")
    List<ChildCategory> findAllByParentId(@Param("parentId") Long parentId);

}
