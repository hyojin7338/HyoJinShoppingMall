package com.example.ntmyou.Exception;

public class CartItemEmpty extends RuntimeException {
    public CartItemEmpty(String s) {
        super(s);
    }
}
