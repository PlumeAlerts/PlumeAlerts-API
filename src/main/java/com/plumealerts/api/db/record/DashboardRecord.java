package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardRecord {

    private final String channelId;
    private final String userId;
    private final String type;
    private final short x;
    private final short y;
    private final short width;
    private final short height;
    private final boolean show;

    public DashboardRecord(ResultSet rs) throws SQLException {
        this.channelId = rs.getString("channel_id");
        this.userId = rs.getString("user_id");
        this.type = rs.getString("type");
        this.x = rs.getShort("x");
        this.y = rs.getShort("y");
        this.width = rs.getShort("width");
        this.height = rs.getShort("height");
        this.show = rs.getBoolean("show");
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getWidth() {
        return width;
    }

    public short getHeight() {
        return height;
    }

    public boolean isShow() {
        return show;
    }
}
