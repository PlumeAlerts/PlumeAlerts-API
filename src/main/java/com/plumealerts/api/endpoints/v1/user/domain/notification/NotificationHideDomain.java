package com.plumealerts.api.endpoints.v1.user.domain.notification;

public class NotificationHideDomain {

    private long id;
    private boolean hide;

    public NotificationHideDomain() {
    }

    public NotificationHideDomain(long id, boolean hide) {
        this.id = id;
        this.hide = hide;
    }

    public long getId() {
        return id;
    }

    public boolean isHide() {
        return hide;
    }
}
