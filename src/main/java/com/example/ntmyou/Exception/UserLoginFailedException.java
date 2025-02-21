package com.example.ntmyou.Exception;

public class UserLoginFailedException extends RuntimeException {
    public UserLoginFailedException(String s) {
        super(s);
    }
}
