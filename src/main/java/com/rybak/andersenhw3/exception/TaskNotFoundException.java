package com.rybak.andersenhw3.exception;

public class TaskNotFoundException extends TaskManagerGlobalException {
    public TaskNotFoundException(String message) {
        super(message, 404);
    }
}
