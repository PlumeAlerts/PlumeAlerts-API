package com.plumealerts.api.endpoints.v1.domain;

public class Data extends Domain {
    private Object data;

    public Data(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
