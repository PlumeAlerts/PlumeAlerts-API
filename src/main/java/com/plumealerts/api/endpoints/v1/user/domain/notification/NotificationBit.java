package com.plumealerts.api.endpoints.v1.user.domain.notification;

import com.plumealerts.api.db.record.NotificationBitRecord;

public class NotificationBit extends NotificationData {
    private final String username;
    private final int bits;
    private final String message;
    private final boolean anonymous;

    public NotificationBit(NotificationBitRecord notification) {
        super(notification);
        this.username = notification.getMessageUsername();
        this.bits = notification.getBits();
        this.message = notification.getMessage();
        this.anonymous = notification.isAnonymous();
    }

    public String getUsername() {
        return username;
    }

    public int getBits() {
        return bits;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAnonymous() {
        return anonymous;
    }
}
