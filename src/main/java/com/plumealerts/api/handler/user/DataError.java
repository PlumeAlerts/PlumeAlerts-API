package com.plumealerts.api.handler.user;

import com.plumealerts.api.endpoints.v1.domain.Domain;

public class DataError<T> {

    private final T data;
    private final Domain error;

    public DataError(T data) {
        this.data = data;
        this.error = null;
    }

    public DataError(T data, Domain error) {
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public Domain getError() {
        return error;
    }

    public boolean hasError() {
        return this.error != null;
    }

    public static <T> DataError<T> error(Domain error) {
        return new DataError<>(null, error);
    }
}
