package com.example.ntmyou.Product.Mapper;

import com.example.ntmyou.Category.Child.ChildCategory;
import com.example.ntmyou.Category.Parents.ParentsCategory;
import com.example.ntmyou.Category.Sub.SubCategory;
import com.example.ntmyou.Master.Entity.Master;
import com.example.ntmyou.Product.DTO.ProductUpdateRequestDto;
import com.example.ntmyou.Product.DTO.ProductUpdateResponseDto;
import com.example.ntmyou.Product.Entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductUpdateMapper {
    // 상품수정  // updateRequestDto -> Entity
    public void toUpdateEntity ( Product product ,
                                   ProductUpdateRequestDto updateRequestDto,
                                   ParentsCategory parentsId,
                                   ChildCategory childId,
                                   SubCategory SubId,
                                   Master masterId,
                                   String updateMainUrl,
                                   List<String> updateImageUrl) {
        // 수정 내용 없으면 기존 값을 그대로 유지
        if (updateRequestDto.getName() != null) product.setName(updateRequestDto.getName());
        if (updateRequestDto.getContents() != null) product.setContents(updateRequestDto.getContents());
        if (updateRequestDto.getAmount() != null) product.setAmount(updateRequestDto.getAmount());


        // 카테고리 수정 빈 값이면 기존 값을 그대로 유지
        if (parentsId != null) product.setParentsCategory(parentsId);
        if (childId != null) product.setChildCategory(childId);
        if (SubId != null) product.setSubCategory(SubId);
        if (masterId != null) product.setMaster(masterId);

        if (updateMainUrl != null && !updateMainUrl.isEmpty()) {
            product.setMainImgUrl(updateMainUrl);
        }

        if (updateImageUrl != null && !updateImageUrl.isEmpty()) {
            product.setImageUrls(updateImageUrl);
        }
    }


    // 상품 수정 응답 보내기 // Entity -> updateResponseDto
    //
    public ProductUpdateResponseDto toUpdateResponseDto(Product product){
        return ProductUpdateResponseDto.builder()
                .name(product.getName())
                .contents(product.getContents())
                .amount(product.getAmount())
                .parentsCategoryName(product.getParentsCategory() != null ? product.getParentsCategory().getName() : null)
                .childCategoryName(product.getChildCategory() != null ? product.getChildCategory().getName() : null)
                .subCategoryName(product.getSubCategory() != null ? product.getSubCategory().getName() : null)
                .masterName(product.getMaster() != null ? product.getMaster().getName() : null)
                .mainImg(product.getMainImgUrl())
                .imageUrls(product.getImageUrls() != null ? product.getImageUrls() : new ArrayList<>()) // null 방지
                .build();
    }


}
