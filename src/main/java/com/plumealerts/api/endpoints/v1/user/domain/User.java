package com.plumealerts.api.endpoints.v1.user.domain;

import com.plumealerts.api.endpoints.v1.domain.Domain;

public class User extends Domain {
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
}
