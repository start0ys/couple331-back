package com.couple.back.exception;

public class DuplicateLoginException extends RuntimeException {
    
    public DuplicateLoginException(String message) {
        super(message);
    }
}
