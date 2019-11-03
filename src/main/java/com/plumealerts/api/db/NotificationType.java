package com.plumealerts.api.db;

public enum NotificationType {
    FOLLOW("follow"),
    BIT("bit"),
    SUBSCRIPTION("subscription"),
    GIFT_SUBSCRIPTION("gift_subscription"),
    HOST("host"),
    RAID("raid"),
    DONATION("donation");

    private String name;

    NotificationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
