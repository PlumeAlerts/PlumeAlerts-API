package com.plumealerts.api.endpoints.v1.user.domain.notification;

import com.plumealerts.api.db.record.NotificationRecord;

public class NotificationData {
    private final long id;
    private final String type;
    private final boolean hide;
    private final String userId;
    private final long createdAt;

    public NotificationData(NotificationRecord notification) {
        this.id = notification.getId();
        this.type = notification.getType();
        this.hide = notification.isHide();
        this.userId = notification.getUserId();
        this.createdAt = notification.getCreatedAt().toEpochSecond();
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public boolean isHide() {
        return hide;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
