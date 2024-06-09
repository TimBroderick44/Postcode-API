package io.nology.postcode_api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private final HttpStatus statusCode;

    protected BaseException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}