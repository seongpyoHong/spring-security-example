package com.sphong.demo.security;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String msg) {
        super(msg);
    }
}
