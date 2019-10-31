package com.plumealerts.api.db;

import com.plumealerts.api.PlumeAlertsAPI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public final class UserLoginRequestDatabase {

    private static final String FIND_BY_STATE = "SELECT created_at FROM user_login_request WHERE state=?";
    private static final String INSERT_USER_LOGIN_REQUEST = "INSERT INTO user_login_request(state, scopes) VALUES (?, ?)";

    private UserLoginRequestDatabase() {
    }

    public static OffsetDateTime findByState(String state) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_BY_STATE)) {
            stmt.setString(1, state);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject("created_at", OffsetDateTime.class);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertUserLoginRequest(String state, String scope) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(INSERT_USER_LOGIN_REQUEST)) {
            stmt.setString(1, state);
            stmt.setString(2, scope);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
