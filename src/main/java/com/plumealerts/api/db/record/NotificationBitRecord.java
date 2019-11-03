package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationBitRecord extends NotificationRecord {
    private final String messageId;
    private final boolean anonymous;
    private final String message;
    private final String messageUsername;
    private final int bits;
    private final int totalBits;

    public NotificationBitRecord(ResultSet rs) throws SQLException {
        super(rs);
        this.messageId = rs.getString("message_id");
        this.anonymous = rs.getBoolean("anonymous");
        this.message = rs.getString("message");
        this.messageUsername = rs.getString("message_username");

        this.bits = rs.getInt("bits_used");
        this.totalBits = rs.getInt("total_bits");
    }

    public String getMessageId() {
        return messageId;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageUsername() {
        return messageUsername;
    }

    public int getBits() {
        return bits;
    }

    public int getTotalBits() {
        return totalBits;
    }
}
