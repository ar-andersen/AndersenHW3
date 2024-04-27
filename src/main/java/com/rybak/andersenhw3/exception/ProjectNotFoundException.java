package com.rybak.andersenhw3.exception;

public class ProjectNotFoundException extends TaskManagerGlobalException {
    public ProjectNotFoundException(String message) {
        super(message, 404);
    }
}
