package com.plumealerts.api.endpoints.response.error;

public class ErrorResponse {
    private final ErrorType error;
    private final String message;

    public ErrorResponse(ErrorType error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error.error;
    }

    public String getMessage() {
        return message;
    }
}
