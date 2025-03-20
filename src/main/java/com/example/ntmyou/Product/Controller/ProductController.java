package com.example.ntmyou.Product.Controller;

import com.example.ntmyou.Product.DTO.ProductRequestDto;
import com.example.ntmyou.Product.DTO.ProductResponseDto;
import com.example.ntmyou.Product.DTO.ProductUpdateRequestDto;
import com.example.ntmyou.Product.DTO.ProductUpdateResponseDto;
import com.example.ntmyou.Product.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 상품생성
    @PostMapping("/master/createProduct")
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestPart("requestDto") @Valid ProductRequestDto requestDto,  // JSON 데이터 받기
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage, // 대표 이미지
            @RequestPart(value = "subImages", required = false) List<MultipartFile> subImages // 서브 이미지 리스트
    ) {
        // 서비스 호출하여 상품 생성
        ProductResponseDto responseDto = productService.createProduct(
                requestDto,
                mainImage,
                subImages
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 상품수정
    @PatchMapping("/master/productUpdate/{productId}")
    public ResponseEntity<ProductUpdateResponseDto> updateProduct(
            @PathVariable Long productId,
            @RequestPart("updateRequestDto") @Valid ProductUpdateRequestDto updateRequestDto,
            @RequestPart(value = "updateMainUrl", required = false) MultipartFile updateMainUrl,
            @RequestPart(value = "updateImageUrl", required = false) List<MultipartFile> updateImageUrl
    ) {
        ProductUpdateResponseDto updatedProduct = productService.updateProduct(productId
                , updateRequestDto, updateMainUrl, updateImageUrl);
        return ResponseEntity.ok(updatedProduct);
    }

    // 카테고리에 맞는 상품이 조회를 하는 로직이 필요하네..!
    @GetMapping("/products/findByProductAndSubCategory")
    public ResponseEntity<List<ProductResponseDto>> getProductsBySubCategory(
            @RequestParam("subCategoryId") Long subCategoryId) {
        List<ProductResponseDto> responseDto = productService.getProductByCategory(subCategoryId);
        return ResponseEntity.ok(responseDto);
    }

    // 특정 상품만 조회되는 로직도 필요했네..! -> 상품상세페이지 들어갈 때
    // DetailProduct 상품상세 페이지로 들어갔을 때 필요한 API 있어야한다
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        ProductResponseDto responseDto = productService.getProductById(productId);
        return ResponseEntity.ok(responseDto);
    }

    // 특정 판매자가 올린 상품 조회
    @GetMapping("/product/Master/{masterId}")
    public ResponseEntity<List<ProductResponseDto>> getProductByMaster(@PathVariable Long masterId) {
        List<ProductResponseDto> responseDto = productService.getProductByMaster(masterId);
        return ResponseEntity.ok(responseDto);
    }

}
