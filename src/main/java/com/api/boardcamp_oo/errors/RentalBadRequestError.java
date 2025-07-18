package com.api.boardcamp_oo.errors;

public class RentalBadRequestError extends RuntimeException {
    public RentalBadRequestError(String message){
        super(message);
    }
    
}
