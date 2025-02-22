package com.example.ntmyou.QnA.Entity.Enum;

public enum GeneralQuestionType {
    // 일바유저가 운영자에게 남기는 문의 유형
    ACCOUNT_ISSUE("계정 관련 문제 (비밀번호 변경, 탈퇴 요청 등)"),
    ORDER_ISSUE("주문 관련 문의 (배송 지연, 주문 취소 등)"),
    PAYMENT_PROBLEM("결제 문제 (결제 오류, 환불 요청 등)"),
    COUPON_ISSUE("쿠폰 및 프로모션 관련 문의"),
    PRODUCT_INQUIRY("특정 상품 관련 문의 (재입고 요청 등)"),
    GENERAL_FEEDBACK("일반적인 피드백 및 건의사항"),
    REPORT_ISSUE("신고 관련 문의 (리뷰 신고, 불법 상품 신고 등)"),

    // (Master)가 운영자(Admin)에게 남기는 문의 유형
    PRODUCT_REGISTRATION("상품 등록 관련 문의"),
    SELLER_POLICY(" 판매 정책 관련 문의"),
    PAYMENT_SETTLEMENT("정산 및 수익금 관련 문의"),
    ACCOUNT_VERIFICATION(" 판매자 계정 인증 및 승인 요청"),
    STORE_MANAGEMENT("스토어 관리 관련 문의"),
    ADVERTISEMENT("광고 및 마케팅 관련 문의"),
    OTHER(" 기타 문의");

    private final String description;

    GeneralQuestionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return  description;
    }
}
