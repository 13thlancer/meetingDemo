package com.v4ward.core.exception;

public class UnknownSessionException extends RuntimeException {
    public UnknownSessionException() {
    }

    public UnknownSessionException(String message) {
        super(message);
    }

    public UnknownSessionException(Throwable cause) {
        super(cause);
    }

    public UnknownSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
