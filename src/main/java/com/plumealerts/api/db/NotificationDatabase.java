package com.plumealerts.api.db;

import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.record.NotificationRecord;
import com.plumealerts.api.db.record.TwitchFollowerRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class NotificationDatabase {

    private static final String FIND_NOTIFICATION = "SELECT * FROM notification WHERE channel_id = ? ORDER BY created_at DESC LIMIT 15";
    private static final String UPDATE_NOTIFICATION = "UPDATE notification SET hide=? WHERE id=?";
    private static final String FIND_FOLLOW_BY_ID = "SELECT * FROM twitch_followers WHERE notification_id = ?";

    private NotificationDatabase() {
    }

    public static List<NotificationRecord> getNotifications(String userId) {
        List<NotificationRecord> notifications = new ArrayList<>();

        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_NOTIFICATION)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new NotificationRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static TwitchFollowerRecord findFollowNotifications(Long id) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_FOLLOW_BY_ID)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TwitchFollowerRecord(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateNotification(long id, boolean hide) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(UPDATE_NOTIFICATION)) {
            stmt.setBoolean(1, hide);
            stmt.setLong(2, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
