package com.plumealerts.api.twitch.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class Token {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("scope")
    private String[] scope;

    @JsonProperty("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public OffsetDateTime getExpire() {
        return OffsetDateTime.now().plusSeconds(expiresIn);
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String[] getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
