package com.plumealerts.api.utils.permission;

public enum Permission {
    USER("user"),
    NOTIFICATION_VIEW("notification_view"),
    NOTIFICATION_EDIT("notification_edit"),
    ;

    private final String name;

    Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
