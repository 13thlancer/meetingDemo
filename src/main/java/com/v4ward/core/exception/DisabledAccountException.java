package com.v4ward.core.exception;

public class DisabledAccountException extends RuntimeException {

    public DisabledAccountException() {
    }

    public DisabledAccountException(String message) {
        super(message);
    }

    public DisabledAccountException(Throwable cause) {
        super(cause);
    }

    public DisabledAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
