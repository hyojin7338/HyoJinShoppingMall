package com.example.ntmyou.Product.Service;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.example.ntmyou.Category.Child.ChildRepository;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Parents.ParentsRepository;
import com.example.ntmyou.Category.Sub.SubCategory;
import com.example.ntmyou.Category.Sub.SubRepository;
import com.example.ntmyou.Config.S3.AwsS3Service;
import com.example.ntmyou.Exception.*;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Master.Repository.MasterRepository;
import com.example.ntmyou.Product.DTO.*;
import com.example.ntmyou.Product.Entity.Product;
import com.example.ntmyou.Product.Entity.ProductSize;
import com.example.ntmyou.Product.Mapper.ProductAdjustCntMapper;
import com.example.ntmyou.Product.Mapper.ProductMapper;
import com.example.ntmyou.Product.Mapper.ProductSizeMapper;
import com.example.ntmyou.Product.Mapper.ProductUpdateMapper;
import com.example.ntmyou.Product.Repository.ProductRepository;

import com.example.ntmyou.Product.Repository.ProductSizeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final MasterRepository masterRepository;
    private final ParentsRepository parentsRepository;
    private final ChildRepository childRepository;
    private final SubRepository subRepository;
    private final ProductRepository productRepository;

    private final AwsS3Service awsS3Service;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductSizeRepository productSizeRepository;

    private final ProductUpdateMapper productUpdateMapper;

    // 상품 생성
    @Transactional
    public ProductResponseDto createProduct(
            ProductRequestDto requestDto
            , MultipartFile mainImage
            , List<MultipartFile> subImages) {

        // 상품 생성
        ParentsCategory parentsCategory = parentsRepository.findById(requestDto.getParentsCategoryId())
                .orElseThrow(() -> new ParentsCategoryNotFoundException("대분류가 존재하지 않습니다."));

        ChildCategory childCategory = childRepository.findById(requestDto.getChildCategoryId())
                .orElseThrow(() -> new ChildCategoryNotFoundException("중분류가 존재하지 않습니다."));

        SubCategory subCategory = subRepository.findById(requestDto.getSubCategoryId())
                .orElseThrow(() -> new SubCategoryNotFoundException("소분류가 존재하지 않습니다."));

        Master master = masterRepository.findById(requestDto.getMasterId())
                .orElseThrow(() -> new MasterNotFoundException("존재하지 않는 관리자입니다."));

        // 상품Id 중복되지 않는지 조회
        if (productRepository.findByCode(requestDto.getCode()).isPresent()) {
            throw new ProductCodeAlreadyExistsException("이미 존재하는 상품코드 입니다.");
        }

        // 상품 가격이 0원 이상이여야한다  // ex) 가격이 마이너스 또는 0원이면 안됨
        if (requestDto.getAmount() == null || requestDto.getAmount() <= 0 ) {
            throw new ProductAmountAndCntException("가격과 재고는 0 이상이어야 합니다.");
        }

        // 대표 이미지 업로드 처리 (예외 발생 시 기존 이미지 유지)
        String newMainImgUrl = master.getMainImgUrl(); // 기존 대표 이미지 유지
        if (mainImage != null && !mainImage.isEmpty()) {
            try {
                List<String> uploadedMainImage = awsS3Service.uploadFile(List.of(mainImage));
                if (!uploadedMainImage.isEmpty()) {
                    //  기존 대표 이미지가 존재할 때만 삭제 (불필요한 S3 파일 삭제)
                    if (newMainImgUrl != null && !newMainImgUrl.isEmpty()) {
                        awsS3Service.deleteFile(newMainImgUrl);
                    }
                    newMainImgUrl = uploadedMainImage.get(0);
                }
            } catch (Exception e) {
                throw new MainFileUploadException("대표 이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        }

       // 기존 서브 이미지 유지하면서 새로운 서브 이미지만 추가
        List<String> existingImageUrls = master.getImageUrls() != null ? master.getImageUrls() : new ArrayList<>();
        List<String> newImageUrls = new ArrayList<>(existingImageUrls); // 기존 이미지 유지

        if (subImages != null && !subImages.isEmpty()) {
            try {
                List<String> uploadedSubImages = awsS3Service.uploadFile(subImages);
                if (!uploadedSubImages.isEmpty()) {
                    // 기존 서브 이미지 + 신규 서브 이미지 병합
                    newImageUrls.addAll(uploadedSubImages);
                }
            } catch (Exception e) {
                throw new SubFileUploadException("서브 이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        }

        Product product = new ProductMapper().toEntity(requestDto, parentsCategory, childCategory, subCategory, master);
        product.setMainImgUrl(newMainImgUrl);
        product.setImageUrls(newImageUrls);

        productRepository.save(product);

        // mapper를 사용하여 entity -> responseDto 넘어가는거잖아
        return new ProductMapper().toResponseDto(product);

    }


    // 상품 수정
    @Transactional
    public ProductUpdateResponseDto updateProduct(Long productId
            , ProductUpdateRequestDto updateRequestDto
            , MultipartFile updateMainUrl
            , List<MultipartFile> updateImageUrl
    ) {

        // 상품이 있는지 조회를 해
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다. 다시 확인해주세요"));


        // 변경할 정보가 있을 경우에만 업데이트
        if (updateRequestDto.getName() != null)product.setName(updateRequestDto.getName());
        if (updateRequestDto.getContents() != null) product.setContents(updateRequestDto.getContents());
        if (updateRequestDto.getAmount() != null) product.setAmount(updateRequestDto.getAmount());

        // 카테고리 변경
        ParentsCategory newParentsCategory = (updateRequestDto.getParentsCategoryId() != null) ?
                parentsRepository.findById(updateRequestDto.getParentsCategoryId())
                        .orElseThrow(() -> new ParentsCategoryNotFoundException("대분류가 존재하지 않습니다.")) : product.getParentsCategory();

        ChildCategory newChildCategory = (updateRequestDto.getChildCategoryId() != null) ?
                childRepository.findById(updateRequestDto.getChildCategoryId())
                        .orElseThrow(() -> new ChildCategoryNotFoundException("중분류가 존재하지 않습니다.")) : product.getChildCategory();

        SubCategory newSubCategory = (updateRequestDto.getSubCategoryId() != null) ?
                subRepository.findById(updateRequestDto.getSubCategoryId())
                        .orElseThrow(() -> new SubCategoryNotFoundException("소분류가 존재하지 않습니다.")) : product.getSubCategory();

        Master newMaster = (updateRequestDto.getMasterId() != null) ?
                masterRepository.findById(updateRequestDto.getMasterId())
                        .orElseThrow(() -> new MasterNotFoundException("판매자가 존재하지 않습니다.")) : product.getMaster();
        // 대표 이미지 업데이트
        String newMainImgUrl = product.getMainImgUrl(); // 기존 대표 이미지 유지
        if (updateMainUrl != null && !updateMainUrl.isEmpty()) {
            try {
                List<String> uploadedMainImage = awsS3Service.uploadFile(List.of(updateMainUrl));
                if (!uploadedMainImage.isEmpty()) {
                    if (newMainImgUrl != null && !newMainImgUrl.isEmpty()) {
                        awsS3Service.deleteFile(newMainImgUrl);
                    }
                    newMainImgUrl = uploadedMainImage.get(0);
                }
            } catch (Exception e) {
                throw new MainFileUploadException("대표 이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        }

        // 서브 이미지 업데이트 (기존 유지 + 새로운 이미지 추가)
        List<String> existingImageUrls = product.getImageUrls() != null ? product.getImageUrls() : new ArrayList<>();
        List<String> newImageUrls = new ArrayList<>(existingImageUrls);

        if (updateImageUrl != null && !updateImageUrl.isEmpty()) {
            try {
                List<String> uploadedSubImages = awsS3Service.uploadFile(updateImageUrl);
                if (!uploadedSubImages.isEmpty()) {
                    newImageUrls.addAll(uploadedSubImages);
                }
            } catch (Exception e) {
                throw new SubFileUploadException("서브 이미지 업로드 중 오류 발생: " + e.getMessage());
            }
        }

        // 상품 사이즈 넣는곳 
        if (updateRequestDto.getSizes() != null) {
            for (ProductSizeResponseDto sizeDto : updateRequestDto.getSizes()) {
                ProductSize productSize = productSizeRepository.findById(sizeDto.getProductSizeId())
                        .orElseThrow(() -> new ProductSizeAndCntNotFoundException("해당 사이즈 정보가 존재하지 않습니다."));

                productSize.setSize(sizeDto.getSize());
                productSize.setCnt(sizeDto.getCnt()); // 재고 업데이트
                productSizeRepository.save(productSize);
            }
        }

        // **상품 엔티티 업데이트**
        productUpdateMapper.toUpdateEntity(product, updateRequestDto, newParentsCategory, newChildCategory, newSubCategory, newMaster, newMainImgUrl, newImageUrls);

        // **수정된 상품 정보 반환**
        return productUpdateMapper.toUpdateResponseDto(product);

    }

    // 상품 현 재고 수정
    @PersistenceContext
    private EntityManager em;

    // 재고조정 재고 증가시키기
    @Transactional
    public ProductAdjustCntResponseDto increaseProductSizeCnt(Long productSizeId, ProductAdjustCntRequestDto requestDto) {
        ProductSize productSize = productSizeRepository.findById(productSizeId)
                .orElseThrow(() -> new ProductSizeAndCntNotFoundException("사이즈 및 재고를 찾을 수 없습니다."));

        int currentCnt = productSize.getCnt() != null ? productSize.getCnt() : 0;
        int adjustedCnt = requestDto.getAdjustCnt() != null ? requestDto.getAdjustCnt() : 0;

        System.out.println("상승 :" + currentCnt + "AND" + adjustedCnt );

        productSize.setCnt(currentCnt + adjustedCnt);

        return ProductAdjustCntMapper.toResponseDto(productSize);
    }

    // 재고조정 재고 감소시키기
    @Transactional
    public ProductAdjustCntResponseDto decreaseProductSizeCnt(Long productSizeId, ProductAdjustCntRequestDto requestDto) {
        ProductSize productSize = productSizeRepository.findById(productSizeId)
                .orElseThrow(() -> new ProductSizeAndCntNotFoundException("사이즈 및 재고를 찾을 수 없습니다."));

        int currentCnt = productSize.getCnt() != null ? productSize.getCnt() : 0;
        int adjustedCnt = requestDto.getAdjustCnt() != null ? requestDto.getAdjustCnt() : 0;

        System.out.println("하락 currentCnt :" + currentCnt + " AND adjustedCnt : " + adjustedCnt );

        int newCnt = currentCnt - adjustedCnt;
        if (newCnt < 0) newCnt = 0; // 음수 방지

        productSize.setCnt(newCnt);

        return ProductAdjustCntMapper.toResponseDto(productSize);
    }

    // 선택한 카테고리에 맞는 상품이 조회가 되어야한다 // 2025-02-26
    // 대분류 -> 중분류 -> 소분류
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductByCategory(Long subCategoryId) {

        List<Product> products = productRepository.findByProductAndSubCategory(subCategoryId);

        return products.stream()
                .map(product -> new ProductMapper().toResponseDto(product))
                .collect(Collectors.toList());
    }

    // 특정 상품만 조회 -> 상품 상세페이지
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findProductWithImages(productId)
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다."));
        return new ProductMapper().toResponseDto(product);
    }

    // 특정판매자가 상품을 등록한 것을 확인하기 위해서 // 2025-03-23
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductByMaster(Long masterId) {
        List<Product> products = productRepository.findByMasterId(masterId);

        for (Product product : products) {
            Hibernate.initialize(product.getImageUrls());
        }

        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }



}
