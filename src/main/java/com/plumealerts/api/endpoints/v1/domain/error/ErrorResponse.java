package com.plumealerts.api.endpoints.v1.domain.error;

import com.plumealerts.api.endpoints.v1.domain.Domain;

public class ErrorResponse extends Domain {
    private final ErrorType type;
    private final String message;

    public ErrorResponse(ErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getError() {
        return type.error;
    }

    public String getMessage() {
        return message;
    }
}
