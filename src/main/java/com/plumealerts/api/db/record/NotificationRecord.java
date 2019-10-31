package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class NotificationRecord {
    private final long id;
    private final String channelId;
    private final String userId;
    private final OffsetDateTime createdAt;
    private final boolean hide;
    private final String type;

    public NotificationRecord(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.channelId = rs.getString("channel_id");
        this.userId = rs.getString("user_id");
        this.createdAt = rs.getObject("created_at", OffsetDateTime.class);
        this.hide = rs.getBoolean("hide");
        this.type = rs.getString("type");
    }

    public long getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUserId() {
        return userId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isHide() {
        return hide;
    }

    public String getType() {
        return type;
    }
}
