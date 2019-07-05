package com.plumealerts.api.twitch.response;

public class SuccessfulResponse<T> {

    private T data;

    public T getData() {
        return this.data;
    }
}
