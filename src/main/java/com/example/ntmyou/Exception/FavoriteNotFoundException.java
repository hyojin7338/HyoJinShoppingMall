package com.example.ntmyou.Exception;

public class FavoriteNotFoundException extends RuntimeException{
    public FavoriteNotFoundException(String s) {
        super(s);
    }
}
