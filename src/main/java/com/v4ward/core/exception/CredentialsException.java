package com.v4ward.core.exception;

public class CredentialsException extends RuntimeException {

    public CredentialsException() {
    }

    public CredentialsException(String message) {
        super(message);
    }

    public CredentialsException(Throwable cause) {
        super(cause);
    }

    public CredentialsException(String message, Throwable cause) {
        super(message, cause);
    }


}
