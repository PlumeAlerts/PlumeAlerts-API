package com.plumealerts.api.handler.user;

import com.plumealerts.api.db.UserLoginRequestDatabase;

import java.time.OffsetDateTime;

/**
 * Handler for authentication
 */
public final class AuthHandler {

    private AuthHandler() {
    }

    /**
     * Checks to see if the state sent to the client is the same returned
     *
     * @param state The state previously sent to the client
     * @return True if the request was sent to the client and is less then 5 minutes old
     */
    public static boolean isLoginRequestValid(String state) {
        OffsetDateTime createdAt = UserLoginRequestDatabase.findByState(state);

        if (createdAt == null) {
            return false;
        }

        //TODO Rewrite table
//        loginRequestRecord.delete();

        // Expire state after 30 minutes, force them to login in again.
        return OffsetDateTime.now().isBefore(createdAt.plusMinutes(5));
    }
}
