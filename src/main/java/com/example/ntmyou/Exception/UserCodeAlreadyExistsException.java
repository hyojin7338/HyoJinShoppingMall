package com.example.ntmyou.Exception;

public class UserCodeAlreadyExistsException extends RuntimeException {
    public UserCodeAlreadyExistsException(String s) {
        super(s);
    }
}
