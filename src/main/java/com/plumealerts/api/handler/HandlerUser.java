package com.plumealerts.api.handler;

import com.github.twitch4j.helix.domain.User;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.NotificationRecord;
import com.plumealerts.api.db.tables.records.TwitchFollowersRecord;
import com.plumealerts.api.db.tables.records.UsersRecord;

import java.util.List;

import static com.plumealerts.api.db.Tables.*;

public class HandlerUser {

    public static boolean insertUser(User user) {
        int i = PlumeAlertsAPI.dslContext().insertInto(USERS, USERS.ID, USERS.EMAIL, USERS.LOGIN, USERS.DISPLAY_NAME, USERS.BROADCASTER_TYPE, USERS.TYPE, USERS.VIEW_COUNT)
                .values(user.getId().toString(), user.getEmail(), user.getLogin(), user.getDisplayName(), user.getBroadcasterType(), user.getType(), Long.valueOf(user.getViewCount()))
                .execute();

        return i == 1;
    }

    public static UsersRecord findUser(String userId) {
        return PlumeAlertsAPI.dslContext().selectFrom(USERS)
                .where(USERS.ID.eq(userId))
                .fetchOne();
    }

    public static boolean updateUser(User user) {
        int i = PlumeAlertsAPI.dslContext().update(USERS)
                .set(USERS.LOGIN, user.getLogin())
                .set(USERS.DISPLAY_NAME, user.getDisplayName())
                .set(USERS.BROADCASTER_TYPE, user.getBroadcasterType())
                .set(USERS.TYPE, user.getType())
                .set(USERS.VIEW_COUNT, Long.valueOf(user.getViewCount()))
                .set(USERS.REFRESH_LOGIN, false)
                .where(USERS.ID.eq(user.getId().toString()))
                .execute();

        return i == 1;
    }

    public static List<NotificationRecord> findUserNotifications(String userId) {
        //TODO Add pagination
        return PlumeAlertsAPI.dslContext().selectFrom(NOTIFICATION)
                .where(NOTIFICATION.CHANNEL_ID.eq(userId))
                .orderBy(NOTIFICATION.CREATED_AT.desc())
                .limit(15)
                .fetch();
    }

    public static TwitchFollowersRecord findFollowNotifications(Long id) {
        return PlumeAlertsAPI.dslContext().selectFrom(TWITCH_FOLLOWERS)
                .where(TWITCH_FOLLOWERS.NOTIFICATION_ID.eq(id))
                .fetchOne();
    }
}
