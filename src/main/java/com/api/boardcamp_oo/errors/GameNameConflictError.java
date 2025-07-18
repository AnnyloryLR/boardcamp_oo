package com.api.boardcamp_oo.errors;

public class GameNameConflictError extends RuntimeException {
    public GameNameConflictError(String message){
        super(message);
    }
}
