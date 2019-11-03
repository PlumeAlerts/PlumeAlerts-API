package com.plumealerts.api.endpoints.v1.user.domain.notification;

import com.plumealerts.api.db.record.NotificationGiftSubscriptionRecord;

public class NotificationGiftSubscription extends NotificationData {
    private final String username;
    private final boolean anonymous;
    private final String subPlan;
    private String gifterUsername;

    public NotificationGiftSubscription(NotificationGiftSubscriptionRecord notification) {
        super(notification);

        this.username = notification.getUsername();
        this.anonymous = notification.isAnonymous();
        this.subPlan = notification.getSubPlan();
        if (this.anonymous) {
            this.gifterUsername = notification.getGifterUsername();
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public String getSubPlan() {
        return subPlan;
    }

    public String getGifterUsername() {
        return gifterUsername;
    }
}
