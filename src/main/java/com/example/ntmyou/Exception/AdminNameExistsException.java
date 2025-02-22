package com.example.ntmyou.Exception;

public class AdminNameExistsException extends RuntimeException {
    public AdminNameExistsException(String s) {
        super(s);
    }
}
