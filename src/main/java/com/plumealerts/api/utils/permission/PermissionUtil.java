package com.plumealerts.api.utils.permission;

import com.plumealerts.api.handler.db.DatabaseUser;
import com.plumealerts.api.utils.Validate;
import io.undertow.server.HttpServerExchange;
import org.jooq.Record1;

public class PermissionUtil {

    /**
     * Returns the token userId if me is used else fetch the id from the username supplied
     *
     * @param exchange         The {@link HttpServerExchange} for the request
     * @param defaultChannelId The default channelId if me is used.
     * @return The channelId of the request or the userId from the token
     */
    public static String getChannelId(HttpServerExchange exchange, String defaultChannelId) {
        String username = Validate.getPathParam(exchange, "username");
        if (username == null || username.equalsIgnoreCase("me")) {
            return defaultChannelId;
        }

        Record1<String> otherUserId = DatabaseUser.findUserByUsername(username);
        if (otherUserId == null)
            return null;
        return otherUserId.value1();
    }

    /**
     * Checks to see if the user has permission
     *
     * @param channelId  The channel data the user is requesting
     * @param userId     The user requesting the data
     * @param permission The permission needed to view the data
     * @return If the channelId and userId are the same returns true, else will check the DB to see if the user has permission
     */
    public static boolean hasPermission(String channelId, String userId, Permission permission) {
        if (channelId.equalsIgnoreCase(userId))
            return true;
        return DatabaseUser.findPermission(channelId, userId, permission.getName());
    }
}
