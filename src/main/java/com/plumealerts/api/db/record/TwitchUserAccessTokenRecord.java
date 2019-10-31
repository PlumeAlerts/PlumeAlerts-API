package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TwitchUserAccessTokenRecord {
    private final String accessToken;
    private final String refreshToken;
    private final OffsetDateTime expiredAt;

    public TwitchUserAccessTokenRecord(ResultSet executeQuery) throws SQLException {
        this.accessToken = executeQuery.getString("access_token");
        this.refreshToken = executeQuery.getString("refresh_token");
        this.expiredAt = executeQuery.getObject("expired_at", OffsetDateTime.class);
    }

    public TwitchUserAccessTokenRecord(String accessToken, String refreshToken, OffsetDateTime expiredAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public OffsetDateTime getExpiredAt() {
        return expiredAt;
    }
}
