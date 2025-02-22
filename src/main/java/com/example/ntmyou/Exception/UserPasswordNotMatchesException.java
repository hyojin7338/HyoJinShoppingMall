package com.example.ntmyou.Exception;

public class UserPasswordNotMatchesException extends RuntimeException {
    public UserPasswordNotMatchesException(String s) {
        super(s);
    }
}
