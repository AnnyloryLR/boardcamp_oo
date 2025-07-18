package com.api.boardcamp_oo.errors;

public class RentalUnprocessableEntityError extends RuntimeException{
    public RentalUnprocessableEntityError(String message){
        super(message);
    }
}
