package com.plumealerts.api.endpoints.v1.auth.domain;

public class AccessToken {

    private String accessToken;
    private String refreshToken;

    /**
     * Seconds
     */
    private long expiredAt;
    private long refreshExpiredAt;

    public AccessToken(String accessToken, String refreshToken, long expiredAt, long refreshExpiredAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
        this.refreshExpiredAt = refreshExpiredAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpiredAt() {
        return expiredAt;
    }

    public long getRefreshExpiredAt() {
        return refreshExpiredAt;
    }
}
