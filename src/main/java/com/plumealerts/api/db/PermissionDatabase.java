package com.plumealerts.api.db;

import com.plumealerts.api.PlumeAlertsAPI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionDatabase {

    private static final String FIND_PERMISSION = "SELECT count() FROM user_permission WHERE channel_id=? AND user_id=? AND permission=?";

    private PermissionDatabase() {
    }

    public static boolean findPermission(String channelId, String userId, String permission) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_PERMISSION)) {
            stmt.setString(1, channelId);
            stmt.setString(2, userId);
            stmt.setString(3, permission);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    if (rs.getInt(1) == 1) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
