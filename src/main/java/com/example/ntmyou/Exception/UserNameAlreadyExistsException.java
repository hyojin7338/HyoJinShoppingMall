package com.example.ntmyou.Exception;

public class UserNameAlreadyExistsException extends RuntimeException {
    public UserNameAlreadyExistsException(String s) {
        super(s);
    }
}
