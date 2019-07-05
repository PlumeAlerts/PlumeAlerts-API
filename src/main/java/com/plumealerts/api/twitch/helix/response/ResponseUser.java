package com.plumealerts.api.twitch.helix.response;

import com.squareup.moshi.Json;

public class ResponseUser {

    @Json(name = "id")
    private String id;

    @Json(name = "login")
    private String login;

    @Json(name = "email")
    private String email;

    @Json(name = "display_name")
    private String displayName;

    @Json(name = "description")
    private String description;

    @Json(name = "broadcaster_type")
    private String broadcastType;

    @Json(name = "offline_image_url")
    private String offlineImageURL;

    @Json(name = "profile_image_url")
    private String profileImageURL;

    @Json(name = "type")
    private String type;

    @Json(name = "view_count")
    private long viewCount;

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getBroadcastType() {
        return broadcastType;
    }

    public String getOfflineImageURL() {
        return offlineImageURL;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public String getType() {
        return type;
    }

    public long getViewCount() {
        return viewCount;
    }
}
