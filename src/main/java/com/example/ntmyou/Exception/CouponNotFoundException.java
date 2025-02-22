package com.example.ntmyou.Exception;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(String s) {
        super(s);
    }
}
