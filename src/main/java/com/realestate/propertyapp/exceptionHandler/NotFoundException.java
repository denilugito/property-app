package com.realestate.propertyapp.exceptionHandler;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
