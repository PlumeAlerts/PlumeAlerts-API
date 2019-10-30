CREATE TABLE user_permission
(
    CHANNEL_ID VARCHAR(36),
    USER_ID    VARCHAR(36),
    PERMISSION TEXT,

    PRIMARY KEY (CHANNEL_ID, USER_ID, PERMISSION),
    FOREIGN KEY (CHANNEL_ID) REFERENCES users (ID),
    FOREIGN KEY (USER_ID) REFERENCES users (ID)
);

CREATE TABLE user_permission_pending
(
    CHANNEL_ID VARCHAR(36),
    USER_ID    VARCHAR(36),
    PERMISSION TEXT,

    PRIMARY KEY (CHANNEL_ID, USER_ID, PERMISSION),
    FOREIGN KEY (CHANNEL_ID) REFERENCES users (ID)
);

ALTER TABLE dashboard
    RENAME TO dashboard_old;

CREATE TABLE dashboard
(
    CHANNEL_ID VARCHAR(36) NOT NULL,
    USER_ID    VARCHAR(36) NOT NULL,
    TYPE       TEXT        NOT NULL,

    X          SMALLINT    NOT NULL DEFAULT 0,
    Y          SMALLINT    NOT NULL DEFAULT 0,
    WIDTH      SMALLINT    NOT NULL DEFAULT 0,
    HEIGHT     SMALLINT    NOT NULL DEFAULT 0,
    SHOW       BOOL        NOT NULL DEFAULT TRUE,

    PRIMARY KEY (CHANNEL_ID, USER_ID, TYPE),
    FOREIGN KEY (CHANNEL_ID) REFERENCES users (ID),
    FOREIGN KEY (USER_ID) REFERENCES users (ID)
);

INSERT INTO dashboard (CHANNEL_ID, USER_ID, TYPE, X, Y, WIDTH, HEIGHT, SHOW)
SELECT dashboard_old.USER_ID,
       dashboard_old.USER_ID,
       dashboard_old.TYPE,
       dashboard_old.X,
       dashboard_old.Y,
       dashboard_old.WIDTH,
       dashboard_old.HEIGHT,
       dashboard_old.SHOW
FROM dashboard_old;