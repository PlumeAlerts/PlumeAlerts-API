package com.plumealerts.api.twitch.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Validate {
    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("login")
    private String login;

    @JsonProperty("scopes")
    private String[] scopes;

    @JsonProperty("user_id")
    private String userId;

    public String getClientId() {
        return clientId;
    }

    public String getLogin() {
        return login;
    }

    public String[] getScopes() {
        return scopes;
    }

    public String getUserId() {
        return userId;
    }
}
