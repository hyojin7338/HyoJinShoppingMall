package com.example.ntmyou.Exception;

public class CouponAlreadyAppliedException extends RuntimeException {
    public CouponAlreadyAppliedException(String s) {
        super(s);
    }
}
