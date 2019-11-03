package com.plumealerts.api.endpoints.v1.user.domain.notification;

import com.plumealerts.api.db.record.NotificationSubscriptionRecord;

public class NotificationSubscription extends NotificationData {
    private final String username;
    private final String subPlan;
    private final String message;
    private final String context;
    private final int streakMonths;
    private final int cumulativeMonths;

    public NotificationSubscription(NotificationSubscriptionRecord notification) {
        super(notification);

        this.username = notification.getUsername();
        this.subPlan = notification.getSubPlan();
        this.message = notification.getMessage();
        this.context = notification.getContext();
        this.streakMonths = notification.getStreakMonths();
        this.cumulativeMonths = notification.getCumulativeMonths();
    }

    public String getUsername() {
        return username;
    }

    public String getSubPlan() {
        return subPlan;
    }

    public String getMessage() {
        return message;
    }

    public String getContext() {
        return context;
    }

    public int getStreakMonths() {
        return streakMonths;
    }

    public int getCumulativeMonths() {
        return cumulativeMonths;
    }
}
