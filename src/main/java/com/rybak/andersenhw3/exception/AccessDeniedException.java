package com.rybak.andersenhw3.exception;

public class AccessDeniedException extends TaskManagerGlobalException {
    public AccessDeniedException(String message) {
        super(message, 403);
    }
}
