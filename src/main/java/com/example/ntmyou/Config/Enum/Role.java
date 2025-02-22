package com.example.ntmyou.Config.Enum;

public enum Role {
    USER("일반회원"),
    MASTER("판매자"),
    ADMIN("쇼핑몰 운영자");
    private final String description;
    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return  description;
    }

}
