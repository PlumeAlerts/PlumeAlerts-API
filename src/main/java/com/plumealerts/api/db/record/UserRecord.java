package com.plumealerts.api.db.record;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRecord {
    private final String id;
    private final String email;
    private final String login;
    private final String displayName;
    private final String broadcasterType;
    private final String type;
    private final long viewCount;
    private final boolean beta;

    public UserRecord(ResultSet executeQuery) throws SQLException {
        this.id = executeQuery.getString("id");
        this.email = executeQuery.getString("email");
        this.login = executeQuery.getString("login");
        this.displayName = executeQuery.getString("display_name");
        this.broadcasterType = executeQuery.getString("broadcaster_type");
        this.type = executeQuery.getString("type");
        this.viewCount = executeQuery.getLong("view_count");
        this.beta = executeQuery.getBoolean("beta");
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBroadcasterType() {
        return broadcasterType;
    }

    public String getType() {
        return type;
    }

    public long getViewCount() {
        return viewCount;
    }

    public boolean isBeta() {
        return beta;
    }
}
