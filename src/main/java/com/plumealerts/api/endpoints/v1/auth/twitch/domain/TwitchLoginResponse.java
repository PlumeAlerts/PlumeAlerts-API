package com.plumealerts.api.endpoints.v1.auth.twitch.domain;

import com.plumealerts.api.endpoints.v1.domain.Domain;

public class TwitchLoginResponse extends Domain {

    private String accessToken;
    private String refreshToken;

    /**
     * Seconds
     */
    private long expiredAt;
    private long refreshExpiredAt;

    public TwitchLoginResponse(String accessToken, String refreshToken, long expiredAt, long refreshExpiredAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
        this.refreshExpiredAt = refreshExpiredAt;
    }
}
