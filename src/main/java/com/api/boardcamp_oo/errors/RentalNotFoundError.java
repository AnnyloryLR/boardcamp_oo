package com.api.boardcamp_oo.errors;

public class RentalNotFoundError extends RuntimeException {
    public RentalNotFoundError(String message){
        super(message);
    }
}
