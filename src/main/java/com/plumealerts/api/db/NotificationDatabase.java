package com.plumealerts.api.db;

import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.record.*;
import com.plumealerts.api.utils.SQLHandler;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class NotificationDatabase {

    private static final String UPDATE_NOTIFICATION = "UPDATE notification SET hide=? WHERE id=?";
    private static final String FIND_FOLLOW_NOTIFICATION = SQLHandler.readFile("findFollowNotification.sql");
    private static final String FIND_BIT_NOTIFICATION = SQLHandler.readFile("findBitNotification.sql");
    private static final String FIND_SUBSCRIPTION_NOTIFICATION = SQLHandler.readFile("findSubscriptionNotification.sql");
    private static final String FIND_GIFT_SUBSCRIPTION_NOTIFICATION = SQLHandler.readFile("findGiftSubscriptionNotification.sql");

    private static String[] notificationTypes;

    // TODO Temp
    static {
        notificationTypes = new String[NotificationType.values().length];
        for (NotificationType type : NotificationType.values()) {
            notificationTypes[type.ordinal()] = type.getName();
        }
    }

    private NotificationDatabase() {
    }

    public static List<NotificationRecord> getNotifications(String userId, List<NotificationType> ignored) {
        List<NotificationRecord> notifications = new ArrayList<>();

        Array array;

        try {
            array = PlumeAlertsAPI.connection().createArrayOf("TEXT", notificationTypes);
        } catch (SQLException e) {
            e.printStackTrace();
            return notifications;
        }
        if (!ignored.contains(NotificationType.FOLLOW)) {
            notifications.addAll(findFollowNotifications(userId, array));
        }

        if (!ignored.contains(NotificationType.BIT)) {
            notifications.addAll(findBitNotifications(userId, array));
        }

        if (!ignored.contains(NotificationType.SUBSCRIPTION)) {
            notifications.addAll(findSubscriptionNotifications(userId, array));
        }

        if (!ignored.contains(NotificationType.GIFT_SUBSCRIPTION)) {
            notifications.addAll(findGiftSubscriptionNotifications(userId, array));
        }
        return notifications;
    }

    public static List<NotificationFollowRecord> findFollowNotifications(String userId, Array notificationTypes) {
        List<NotificationFollowRecord> notifications = new ArrayList<>();

        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_FOLLOW_NOTIFICATION)) {
            stmt.setString(1, userId);
            stmt.setArray(2, notificationTypes);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new NotificationFollowRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static List<NotificationBitRecord> findBitNotifications(String userId, Array notificationTypes) {
        List<NotificationBitRecord> notifications = new ArrayList<>();

        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_BIT_NOTIFICATION)) {
            stmt.setString(1, userId);
            stmt.setArray(2, notificationTypes);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new NotificationBitRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static List<NotificationSubscriptionRecord> findSubscriptionNotifications(String userId, Array notificationTypes) {
        List<NotificationSubscriptionRecord> notifications = new ArrayList<>();

        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_SUBSCRIPTION_NOTIFICATION)) {
            stmt.setString(1, userId);
            stmt.setArray(2, notificationTypes);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new NotificationSubscriptionRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static List<NotificationGiftSubscriptionRecord> findGiftSubscriptionNotifications(String userId, Array notificationTypes) {
        List<NotificationGiftSubscriptionRecord> notifications = new ArrayList<>();

        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_GIFT_SUBSCRIPTION_NOTIFICATION)) {
            stmt.setString(1, userId);
            stmt.setArray(2, notificationTypes);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new NotificationGiftSubscriptionRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
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
