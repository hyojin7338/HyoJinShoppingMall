package com.example.ntmyou.QnA.Entity.Enum;

public enum ProductQuestionType {
    PRODUCT_INFO("상품 정보 문의"), // 상품 정보 문의
    STOCK("재고 관련 문의"),        // 재고 관련 문의
    DELIVERY("배송 관련 문의"),     // 배송 관련 문의
    RETURN("반품/교환 문의"),       // 반품/교환 문의
    ETC("기타 문의");          // 기타 문의
    private final String description;
    ProductQuestionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return  description;
    }
}
