package com.plumealerts.api.handler.db;

import com.github.twitch4j.helix.domain.User;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.DashboardRecord;
import com.plumealerts.api.db.tables.records.NotificationRecord;
import com.plumealerts.api.db.tables.records.TwitchFollowersRecord;
import com.plumealerts.api.db.tables.records.UsersRecord;
import com.plumealerts.api.endpoints.v1.user.domain.DashboardDomain;
import com.plumealerts.api.handler.user.DashboardType;
import org.jooq.Record1;

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

    public static UsersRecord findUser(String channelId) {
        return PlumeAlertsAPI.dslContext().selectFrom(USERS)
                .where(USERS.ID.eq(channelId))
                .fetchOne();
    }

    public static Record1<String> findUserByUsername(String username) {
        return PlumeAlertsAPI.dslContext().select(USERS.ID)
                .from(USERS)
                .where(USERS.DISPLAY_NAME.equalIgnoreCase(username))
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

    public static List<DashboardRecord> findUserDashboard(String channelId, String userId) {
        return PlumeAlertsAPI.dslContext().selectFrom(DASHBOARD)
                .where(DASHBOARD.CHANNEL_ID.eq(channelId).and(DASHBOARD.USER_ID.eq(userId)))
                .fetch();
    }

    public static void createDefaultDashboard(String channelId, String userId) {
        DatabaseUser.insertDashboard(channelId, userId, DashboardType.CHAT.name(), (short) 7, (short) 0, (short) 3, (short) 20, true);
        DatabaseUser.insertDashboard(channelId, userId, DashboardType.NOTIFICATION.name(), (short) 0, (short) 0, (short) 5, (short) 10, true);
    }

    public static boolean insertDashboard(String channelId, String userId, String type, short x, short y, short width, short height, boolean show) {
        int i = PlumeAlertsAPI.dslContext().insertInto(DASHBOARD, DASHBOARD.CHANNEL_ID, DASHBOARD.USER_ID, DASHBOARD.TYPE, DASHBOARD.X, DASHBOARD.Y, DASHBOARD.WIDTH, DASHBOARD.HEIGHT, DASHBOARD.SHOW)
                .values(channelId, userId, type, x, y, width, height, show)
                .onDuplicateKeyIgnore()
                .execute();

        return i == 1;
    }

    public static boolean updateDashboard(String channelId, String userId, DashboardType type, DashboardDomain dashboard) {
        int i = PlumeAlertsAPI.dslContext().update(DASHBOARD)
                .set(DASHBOARD.X, dashboard.getX())
                .set(DASHBOARD.Y, dashboard.getY())
                .set(DASHBOARD.WIDTH, dashboard.getWidth())
                .set(DASHBOARD.HEIGHT, dashboard.getHeight())
                .set(DASHBOARD.SHOW, dashboard.isShow())
                .where(DASHBOARD.CHANNEL_ID.eq(channelId).and(DASHBOARD.USER_ID.eq(userId)).and(DASHBOARD.TYPE.eq(type.name())))
                .execute();
        return i == 1;
    }

    public static boolean updateNotification(String userId, long id, boolean hide) {
        int i = PlumeAlertsAPI.dslContext().update(NOTIFICATION)
                .set(NOTIFICATION.HIDE, hide)
                .where(NOTIFICATION.ID.eq(id).and(NOTIFICATION.CHANNEL_ID.eq(userId)))
                .execute();
        return i == 1;
    }

    public static boolean findPermission(String channelId, String userId, String name) {
        Record1<Integer> s = PlumeAlertsAPI.dslContext().selectCount()
                .from(USER_PERMISSION)
                .where(USER_PERMISSION.CHANNEL_ID.eq(channelId)
                        .and(USER_PERMISSION.USER_ID.eq(userId))
                        .and(USER_PERMISSION.PERMISSION.eq(name)))
                .fetchOne();
        return s != null && s.value1() == 1;
    }
}
