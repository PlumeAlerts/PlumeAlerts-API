package com.plumealerts.api.endpoints.v1.user.domain.notification;

public class NotificationFollow extends NotificationData {
    private final String username;

    public NotificationFollow(String userId, String username, long createdAt, String type) {
        super(userId, createdAt, type);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
