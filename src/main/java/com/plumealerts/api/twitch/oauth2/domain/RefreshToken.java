package com.plumealerts.api.twitch.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshToken extends Token {
    @JsonProperty("scope")
    private String scope;
}
