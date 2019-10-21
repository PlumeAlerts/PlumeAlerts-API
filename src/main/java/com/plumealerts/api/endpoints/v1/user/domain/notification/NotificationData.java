package com.plumealerts.api.endpoints.v1.user.domain.notification;

public class NotificationData {
    private final long id;
    private final String type;
    private final boolean hide;
    private final String userId;
    private final long createdAt;

    public NotificationData(long id, String type, boolean hide, String userId, long createdAt) {
        this.id = id;
        this.type = type;
        this.hide = hide;
        this.userId = userId;
        this.createdAt = createdAt;
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
