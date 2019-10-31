package com.plumealerts.api.db;

import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.record.TwitchUserAccessTokenRecord;
import com.plumealerts.api.twitch.oauth2.domain.Token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class TwitchUserAccessTokenDatabase {

    private static final String FIND_ACCESS_TOKEN = "SELECT * FROM twitch_user_access_token WHERE user_id = ?";
    private static final String INSERT_ACCESS_TOKEN = "INSERT INTO twitch_user_access_token(user_id, access_token, refresh_token, expired_at) VALUES (?,?,?,?)";
    private static final String UPDATE_ACCESS_TOKEN = "UPDATE twitch_user_access_token SET access_token=?, refresh_token=?, expired_at=?,last_validated=? WHERE user_id=?";

    private static final String UPDATE_LAST_VALIDATED = "UPDATE twitch_user_access_token SET last_validated=? WHERE user_id=?";

    private TwitchUserAccessTokenDatabase() {
    }

    public static boolean insertAccessToken(String userId, Token token) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(INSERT_ACCESS_TOKEN)) {
            stmt.setString(1, userId);
            stmt.setString(2, token.getAccessToken());
            stmt.setString(3, token.getRefreshToken());
            stmt.setObject(4, token.getExpire());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static TwitchUserAccessTokenRecord findAccessTokenByUserId(String userId) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_ACCESS_TOKEN)) {
            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return new TwitchUserAccessTokenRecord(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateValidated(String userId) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(UPDATE_LAST_VALIDATED)) {
            stmt.setObject(1, OffsetDateTime.now());
            stmt.setString(2, userId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static TwitchUserAccessTokenRecord update(String userId, Token token) {
        try {
            try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(UPDATE_ACCESS_TOKEN)) {
                stmt.setString(1, token.getAccessToken());
                stmt.setString(2, token.getRefreshToken());
                stmt.setObject(3, token.getExpire());
                stmt.setObject(4, OffsetDateTime.now());
                stmt.setString(5, userId);

                if (stmt.executeUpdate() == 1) {
                    return new TwitchUserAccessTokenRecord(token.getAccessToken(), token.getRefreshToken(), token.getExpire());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
