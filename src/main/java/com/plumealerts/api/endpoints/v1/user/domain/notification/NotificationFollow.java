package com.plumealerts.api.endpoints.v1.user.domain.notification;

import com.plumealerts.api.db.record.NotificationFollowRecord;

public class NotificationFollow extends NotificationData {
    private final String username;

    public NotificationFollow(NotificationFollowRecord notification) {
        super(notification);
        this.username = notification.getFollowerUsername();
    }

    public String getUsername() {
        return username;
    }
}
