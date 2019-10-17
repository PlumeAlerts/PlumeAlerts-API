package com.plumealerts.api.endpoints.v1.domain;

public class Data<T> extends Domain {
    private T data;

    private Data() {
    }

    public Data(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
