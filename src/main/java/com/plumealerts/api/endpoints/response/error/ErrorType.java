package com.plumealerts.api.endpoints.response.error;

import org.apache.http.HttpStatus;

public enum ErrorType {

    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),
    BAD_REQUEST(HttpStatus.SC_BAD_REQUEST, "Bad Request"),
    ;

    int code;
    String error;

    ErrorType(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }
}
