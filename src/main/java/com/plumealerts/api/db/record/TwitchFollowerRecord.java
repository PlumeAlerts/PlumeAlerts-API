package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TwitchFollowerRecord {
    private final long notificationId;
    private final String followerUsername;

    public TwitchFollowerRecord(ResultSet resultSet) throws SQLException {
        this.notificationId = resultSet.getLong("notification_id");
        this.followerUsername = resultSet.getString("follower_username");
    }

    public long getNotificationId() {
        return notificationId;
    }

    public String getFollowerUsername() {
        return followerUsername;
    }
}
