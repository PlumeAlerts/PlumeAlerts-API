CREATE TABLE users
(
    ID               VARCHAR(36),
    EMAIL            VARCHAR(255) NOT NULL,
    LOGIN            VARCHAR(25)  NOT NULL,
    DISPLAY_NAME     VARCHAR(25)  NOT NULL,
    BROADCASTER_TYPE TEXT,
    TYPE             TEXT,
    VIEW_COUNT       BIGINT       NOT NULL,

    BETA             BOOLEAN DEFAULT FALSE,
    REFRESH_LOGIN    BOOLEAN DEFAULT FALSE,

    PRIMARY KEY (ID)
);

CREATE TABLE scopes
(
    SCOPE TEXT,
    PRIMARY KEY (SCOPE)
);

CREATE TABLE user_login_request
(
    STATE      TEXT,
    CREATED_AT TIMESTAMPTZ DEFAULT now(),

    SCOPES     TEXT NOT NULL,

    PRIMARY KEY (STATE, CREATED_AT)
);

CREATE TABLE twitch_user_access_token
(
    USER_ID        VARCHAR(36),
    ACCESS_TOKEN   TEXT        NOT NULL,
    REFRESH_TOKEN  TEXT        NOT NULL,
    EXPIRED_AT     TIMESTAMPTZ NOT NULL,
    LAST_VALIDATED TIMESTAMPTZ DEFAULT now(),

    PRIMARY KEY (USER_ID),
    FOREIGN KEY (USER_ID) REFERENCES users (ID)
);

INSERT INTO scopes
VALUES ('user:read:email'),
       ('chat:read'),
       ('bits:read'),
       ('channel:read:subscriptions'),
       ('channel_subscriptions');