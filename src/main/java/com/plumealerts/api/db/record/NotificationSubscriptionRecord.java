package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationSubscriptionRecord extends NotificationRecord {

    private final String username;
    private final String displayName;
    private final String subPlan;
    private final int cumulativeMonths;
    private final int streakMonths;
    private final String message;
    private final String context;

    public NotificationSubscriptionRecord(ResultSet rs) throws SQLException {
        super(rs);

        // TODO Move username to the notification table
        // TODO move context to a boolean sub/resub
        // TODO make a sub plan enum

        this.username = rs.getString("recipient_username");
        this.displayName = rs.getString("recipient_display_name");
        this.subPlan = rs.getString("sub_plan");
        this.cumulativeMonths = rs.getInt("cumulative_months");
        this.streakMonths = rs.getInt("streak_months");
        this.message = rs.getString("message");
        this.context = rs.getString("context");
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSubPlan() {
        return subPlan;
    }

    public int getCumulativeMonths() {
        return cumulativeMonths;
    }

    public int getStreakMonths() {
        return streakMonths;
    }

    public String getMessage() {
        return message;
    }

    public String getContext() {
        return context;
    }
}
