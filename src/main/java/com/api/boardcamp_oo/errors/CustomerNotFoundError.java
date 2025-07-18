package com.api.boardcamp_oo.errors;

public class CustomerNotFoundError extends RuntimeException {
    public CustomerNotFoundError(String message){
        super(message);
    }
}
