package com.rybak.andersenhw3.exception;

public class UserAlreadyExistException extends TaskManagerGlobalException {
    public UserAlreadyExistException(String message) {
        super(message, 400);
    }

}
