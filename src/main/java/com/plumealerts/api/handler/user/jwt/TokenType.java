package com.plumealerts.api.handler.user.jwt;

/**
 * The type of token being created
 */
public enum TokenType {
    ACCESS_TOKEN("access"),
    REFRESH_TOKEN("refresh");

    private String type;

    TokenType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
