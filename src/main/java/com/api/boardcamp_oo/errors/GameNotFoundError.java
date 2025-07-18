package com.api.boardcamp_oo.errors;

public class GameNotFoundError extends RuntimeException {
    public GameNotFoundError(String message){
        super(message);
    }
}
