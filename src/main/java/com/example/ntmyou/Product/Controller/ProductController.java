package com.example.ntmyou.Product.Controller;

import com.example.ntmyou.Product.DTO.*;
import com.example.ntmyou.Product.Repository.ProductRepository;
import com.example.ntmyou.Product.Service.ProductService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    // 상품등록
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

    // 상품코드 중복 허용 체크
    @GetMapping("/Product/codeCheck")
    public ResponseEntity<?> codeCheck(@RequestParam String code) {
        boolean exists = productRepository.existsByCode(code);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 상품 코드 입니다.");
        }
        return ResponseEntity.ok("SUCCESS");
    }

    // 상품수정
    @PutMapping("/master/productUpdate/{productId}")
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

    // 상품사이즈 수정
    @PutMapping("/master/productSizeIncrease/{productSizeId}")
    public ResponseEntity<ProductAdjustCntResponseDto> increaseProductSize(@PathVariable Long productSizeId,
                                                                      @RequestBody @Valid ProductAdjustCntRequestDto requestDto) {
        ProductAdjustCntResponseDto responseDto = productService.increaseProductSizeCnt(productSizeId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 상품사이즈 수량 감소
    @PutMapping("/master/productSizeDecrease/{productSizeId}")
    public ResponseEntity<ProductAdjustCntResponseDto> decreaseProductSize(@PathVariable Long productSizeId,
                                                                      @RequestBody ProductAdjustCntRequestDto requestDto) {
        ProductAdjustCntResponseDto responseDto = productService.decreaseProductSizeCnt(productSizeId, requestDto);
        return ResponseEntity.ok(responseDto);
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

    // 메인화면에서 serach 검색기능 활성화
    @GetMapping("/product/search")
    public ResponseEntity<List<SearchProductResponseDto>> searchProducts(@RequestParam String keyword) {
        List<SearchProductResponseDto> results = productService.searchByKeyword(keyword);
        return ResponseEntity.ok(results);
    }

//    @PostMapping("/testDto")
//    public ResponseEntity<String> testDto(@RequestBody ProductAdjustCntRequestDto requestDto) {
//        System.out.println("📌 test-dto에서 받은 requestDto: " + requestDto);
//        System.out.println("📌 test-dto에서 받은 adjustCnt: " + requestDto.getAdjustCnt());
//        return ResponseEntity.ok("adjustCnt = " + requestDto.getAdjustCnt());
//    }


}
