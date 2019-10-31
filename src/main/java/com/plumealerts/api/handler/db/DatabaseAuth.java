package com.plumealerts.api.handler.db;

import com.plumealerts.api.db.UserLoginRequestDatabase;

import java.time.OffsetDateTime;


public class DatabaseAuth {

    private DatabaseAuth() {
    }

    public static boolean isLoginRequestValid(String state) {
        OffsetDateTime createdAt = UserLoginRequestDatabase.findByState(state);

        if (createdAt == null) {
            return false;
        }

        //TODO Rewrite table
//        loginRequestRecord.delete();

        // Expire state after 30 minutes, force them to login in again.
        return OffsetDateTime.now().isBefore(createdAt.plusMinutes(30));
    }
}
