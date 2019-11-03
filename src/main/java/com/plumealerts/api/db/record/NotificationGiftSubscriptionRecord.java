package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationGiftSubscriptionRecord extends NotificationRecord {

    private final String username;
    private final String subPlan;
    private final boolean anonymous;
    private String gifterId;
    private String gifterUsername;

    public NotificationGiftSubscriptionRecord(ResultSet rs) throws SQLException {
        super(rs);

        // TODO Move username to the notification table
        // TODO move context to a boolean sub/resub
        // TODO make a sub plan enum

        this.username = rs.getString("recipient_username");
        this.subPlan = rs.getString("sub_plan");
        this.anonymous = rs.getBoolean("anonymous");
        if (!this.anonymous) {
            this.gifterId = rs.getString("gifter_id");
            this.gifterUsername = rs.getString("gifter_username");
        }
    }

    public String getUsername() {
        return username;
    }

    public String getSubPlan() {
        return subPlan;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public String getGifterId() {
        return gifterId;
    }

    public String getGifterUsername() {
        return gifterUsername;
    }
}
