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



}
