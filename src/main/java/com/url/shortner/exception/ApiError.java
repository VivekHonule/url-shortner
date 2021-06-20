package com.url.shortner.exception;

import org.springframework.http.HttpStatus;

class ApiError {

    private final HttpStatus status;
    private final String message;
    private final String debugMessage;

    ApiError(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.message = message;
        this.debugMessage = ex.toString();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
