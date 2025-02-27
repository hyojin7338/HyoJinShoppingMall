package com.example.ntmyou.Product.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class ProductSize {
    // 상품 사이즈 관련하여 추가 및 수정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productSize_id", nullable = false)
    private Long productSizeId;

    private String size; // "S", "M", "L", "XL", "XXL"

    private Integer cnt; // 사이즈의 재고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
