package com.plumealerts.api.handler.db;

import com.github.twitch4j.helix.domain.User;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.DashboardRecord;
import com.plumealerts.api.db.tables.records.NotificationRecord;
import com.plumealerts.api.db.tables.records.TwitchFollowersRecord;
import com.plumealerts.api.db.tables.records.UsersRecord;
import com.plumealerts.api.endpoints.v1.user.domain.Dashboard;
import com.plumealerts.api.handler.user.DashboardType;

import java.util.List;

import static com.plumealerts.api.db.Tables.*;

public class DatabaseUser {

    private DatabaseUser() {
    }

    public static boolean insertUser(User user) {
        int i = PlumeAlertsAPI.dslContext().insertInto(USERS, USERS.ID, USERS.EMAIL, USERS.LOGIN, USERS.DISPLAY_NAME, USERS.BROADCASTER_TYPE, USERS.TYPE, USERS.VIEW_COUNT)
                .values(user.getId(), user.getEmail(), user.getLogin(), user.getDisplayName(), user.getBroadcasterType(), user.getType(), Long.valueOf(user.getViewCount()))
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
                .where(USERS.ID.eq(user.getId()))
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

    public static List<DashboardRecord> findUserDashboard(String userId) {
        return PlumeAlertsAPI.dslContext().selectFrom(DASHBOARD)
                .where(DASHBOARD.USER_ID.eq(userId))
                .fetch();
    }

    public static boolean insertDashboard(String userId, String type, short x, short y, short width, short height, boolean show) {
        int i = PlumeAlertsAPI.dslContext().insertInto(DASHBOARD, DASHBOARD.USER_ID, DASHBOARD.TYPE, DASHBOARD.X, DASHBOARD.Y, DASHBOARD.WIDTH, DASHBOARD.HEIGHT, DASHBOARD.SHOW)
                .values(userId, type, x, y, width, height, show)
                .onDuplicateKeyIgnore()
                .execute();

        return i == 1;
    }

    public static boolean updateDashboard(String userId, DashboardType type, Dashboard dashboard) {
        int i = PlumeAlertsAPI.dslContext().update(DASHBOARD)
                .set(DASHBOARD.X, dashboard.getX())
                .set(DASHBOARD.Y, dashboard.getY())
                .set(DASHBOARD.WIDTH, dashboard.getWidth())
                .set(DASHBOARD.HEIGHT, dashboard.getHeight())
                .set(DASHBOARD.SHOW, dashboard.isShow())
                .where(DASHBOARD.USER_ID.eq(userId).and(DASHBOARD.TYPE.eq(type.name())))
                .execute();
        return i == 1;
    }
}
