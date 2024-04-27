package com.rybak.andersenhw3.exception;

import lombok.Getter;

@Getter
public class TaskManagerGlobalException extends RuntimeException {

    private int code;

    public TaskManagerGlobalException(String message, int code) {
        super(message);
        this.code = code;
    }

}
