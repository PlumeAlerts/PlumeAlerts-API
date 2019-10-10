package com.plumealerts.api.endpoints.v1.auth.twitch.domain;

public class TwitchLogin {
    private String url;

    public TwitchLogin(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
