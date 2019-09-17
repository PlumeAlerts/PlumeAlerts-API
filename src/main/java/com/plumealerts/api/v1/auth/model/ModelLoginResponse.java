package com.plumealerts.api.v1.auth.model;

public class ModelLoginResponse {

    private String accessToken;
    private String refreshToken;

    /**
     * Seconds
     */
    private long expiredAt;
    private long refreshExpiredAt;

    public ModelLoginResponse(String accessToken, String refreshToken, long expiredAt, long refreshExpiredAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
        this.refreshExpiredAt = refreshExpiredAt;
    }
}
