package com.plumealerts.api.db;

import com.github.twitch4j.helix.domain.User;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.record.UserRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDatabase {

    private static final String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_USER_BY_LOGIN = "SELECT id FROM users WHERE lower(login) = lower(?)";
    private static final String INSERT_USER = "INSERT INTO users (id, email, login, display_name, broadcaster_type, type, view_count) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_USER = "UPDATE users SET login=?,display_name=?,broadcaster_type=?,type=?,view_count=? WHERE id=?";

    private UserDatabase() {
    }

    public static UserRecord findById(String userId) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_USER_BY_ID)) {
            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserRecord(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String findIdByLogin(String login) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_USER_BY_LOGIN)) {
            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertUser(User user) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(INSERT_USER)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setString(4, user.getDisplayName());
            stmt.setString(5, user.getBroadcasterType());
            stmt.setString(6, user.getType());
            stmt.setLong(7, user.getViewCount());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(User user) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(UPDATE_USER)) {
            stmt.setObject(1, user.getId());
            stmt.setString(2, user.getDisplayName());
            stmt.setString(3, user.getBroadcasterType());
            stmt.setString(4, user.getType());
            stmt.setLong(5, user.getViewCount());
            stmt.setString(6, user.getId());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
