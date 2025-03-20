package com.example.ntmyou.Product.Repository;

import com.example.ntmyou.Product.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);

    // 소분류까지 조회 된 상픔 찾기
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.imageUrls WHERE p.subCategory.subCategoryId = :subCategoryId")
    List<Product> findByProductAndSubCategory(@Param("subCategoryId") Long subCategoryId);

    // 특정 상품 조회
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.imageUrls WHERE p.productId = :productId")
    Optional<Product> findProductWithImages(@Param("productId") Long productId);

    // 특정 판매자가 등록한 상품 찾기
    @Query("SELECT p FROM Product p WHERE p.master.masterId = :masterId")
    List<Product> findByMasterId(@Param("masterId")Long masterId);
}
