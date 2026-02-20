package com.realestate.propertyapp.exceptionHandler.dto;

public class ApiError {
    public int status;
    public String message;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
