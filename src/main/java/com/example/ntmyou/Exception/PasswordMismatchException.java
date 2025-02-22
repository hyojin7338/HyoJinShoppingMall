package com.example.ntmyou.Exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String s) {
        super(s);
    }
}
