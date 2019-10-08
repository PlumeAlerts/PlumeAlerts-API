package com.plumealerts.api.endpoints.v1.user.domain.notification;

public class NotificationData {
    public String userId;
    public long createdAt;
    public String type;

    public NotificationData(String userId, long createdAt, String type) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }
}
