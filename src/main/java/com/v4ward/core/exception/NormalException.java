package com.v4ward.core.exception;

public class NormalException extends RuntimeException {
    public NormalException() {
    }

    public NormalException(String message) {
        super(message);
    }

    public NormalException(Throwable cause) {
        super(cause);
    }

    public NormalException(String message, Throwable cause) {
        super(message, cause);
    }
}
