package com.plumealerts.api.endpoints.v1.user.domain.notification;

public class NotificationFollow extends NotificationData {
    private final String username;

    public NotificationFollow(long id, String type, boolean hide, String userId, long createdAt, String username) {
        super(id, type, hide, userId, createdAt);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
