package com.api.boardcamp_oo.errors;

public class CustomerCpfConflictError extends RuntimeException {
    public CustomerCpfConflictError(String message){
        super(message);
    }
}
