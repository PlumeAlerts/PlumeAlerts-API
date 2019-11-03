package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationFollowRecord extends NotificationRecord {
    private final String followerUsername;

    public NotificationFollowRecord(ResultSet rs) throws SQLException {
        super(rs);
        this.followerUsername = rs.getString("follower_username");
    }

    public String getFollowerUsername() {
        return followerUsername;
    }
}
