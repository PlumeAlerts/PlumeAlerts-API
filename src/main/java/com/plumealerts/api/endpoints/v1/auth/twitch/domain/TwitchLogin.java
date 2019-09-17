package com.plumealerts.api.endpoints.v1.auth.twitch.domain;

import com.plumealerts.api.endpoints.v1.domain.Domain;

public class TwitchLogin extends Domain {
    private String url;

    public TwitchLogin(String url) {
        this.url = url;
    }
}
