/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db;


import com.plumealerts.api.db.tables.Scopes;
import com.plumealerts.api.db.tables.UserAccessToken;
import com.plumealerts.api.db.tables.UserLoginRequest;
import com.plumealerts.api.db.tables.Users;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.scopes</code>.
     */
    public static final Scopes SCOPES = com.plumealerts.api.db.tables.Scopes.SCOPES;

    /**
     * The table <code>public.user_access_token</code>.
     */
    public static final UserAccessToken USER_ACCESS_TOKEN = com.plumealerts.api.db.tables.UserAccessToken.USER_ACCESS_TOKEN;

    /**
     * The table <code>public.user_login_request</code>.
     */
    public static final UserLoginRequest USER_LOGIN_REQUEST = com.plumealerts.api.db.tables.UserLoginRequest.USER_LOGIN_REQUEST;

    /**
     * The table <code>public.users</code>.
     */
    public static final Users USERS = com.plumealerts.api.db.tables.Users.USERS;
}
