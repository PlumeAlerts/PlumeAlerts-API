package com.plumealerts.api.handler.db;

import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.UserLoginRequestRecord;

import java.time.OffsetDateTime;

import static com.plumealerts.api.db.Tables.USER_LOGIN_REQUEST;

public class DatabaseAuth {

    private DatabaseAuth() {
    }

    public static boolean isLoginRequestValid(String state) {
        UserLoginRequestRecord loginRequestRecord = PlumeAlertsAPI.dslContext().selectFrom(USER_LOGIN_REQUEST)
                .where(USER_LOGIN_REQUEST.STATE.eq(state))
                .fetchOne();

        if (loginRequestRecord == null) {
            return false;
        }
        loginRequestRecord.delete();

        // Expire state after 30 minutes, force them to login in again.
        if (OffsetDateTime.now().isAfter(loginRequestRecord.getCreatedAt().plusMinutes(30))) {
            return false;
        }
        return true;
    }
}
