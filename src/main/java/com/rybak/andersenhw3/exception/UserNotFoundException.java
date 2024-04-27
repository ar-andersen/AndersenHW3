package com.rybak.andersenhw3.exception;

public class UserNotFoundException extends TaskManagerGlobalException {

    public UserNotFoundException(String message) {
        super(message, 404);
    }

}
