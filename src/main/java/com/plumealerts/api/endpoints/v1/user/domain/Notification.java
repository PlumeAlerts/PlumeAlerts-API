package com.plumealerts.api.endpoints.v1.user.domain;

import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.user.domain.notification.NotificationData;

import java.util.List;

public class Notification extends Domain {
    private List<NotificationData> data;

    public Notification(List<NotificationData> data) {
        this.data = data;
    }
}
