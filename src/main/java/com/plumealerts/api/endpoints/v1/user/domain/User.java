package com.plumealerts.api.endpoints.v1.user.domain;

public class User {
    private final String id;
    private final String login;
    private final String displayName;
    private final Boolean beta;
    private final String broadcasterType;

    public User(String id, String login, String displayName, Boolean beta, String broadcasterType) {
        this.id = id;
        this.login = login;
        this.displayName = displayName;
        this.beta = beta;
        this.broadcasterType = broadcasterType;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Boolean getBeta() {
        return beta;
    }

    public String getBroadcasterType() {
        return broadcasterType;
    }
}
