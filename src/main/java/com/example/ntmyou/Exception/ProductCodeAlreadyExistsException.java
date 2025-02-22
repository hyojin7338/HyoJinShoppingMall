package com.example.ntmyou.Exception;

public class ProductCodeAlreadyExistsException extends RuntimeException {
    public ProductCodeAlreadyExistsException(String s) {
        super(s);
    }
}
