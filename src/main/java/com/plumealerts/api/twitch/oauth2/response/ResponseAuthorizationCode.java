package com.plumealerts.api.twitch.oauth2.response;

import com.squareup.moshi.Json;

import java.sql.Timestamp;

public class ResponseAuthorizationCode {

    @Json(name = "access_token")
    private String accessToken;

    @Json(name = "expires_in")
    private int expiresIn;

    @Json(name = "refresh_token")
    private String refreshToken;

    @Json(name = "scope")
    private String[] scope;

    @Json(name = "token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public Timestamp getExpire() {
        return new Timestamp(System.currentTimeMillis() + expiresIn * 1000);
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
