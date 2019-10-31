package com.plumealerts.api.handler.db;

import com.plumealerts.api.db.DashboardDatabase;
import com.plumealerts.api.handler.user.DashboardType;

public class DatabaseUser {

    private DatabaseUser() {
    }

    public static void createDefaultDashboard(String channelId, String userId) {
        DashboardDatabase.insertOnDuplicateIgnoreDashboard(channelId, userId, DashboardType.CHAT.name(), (short) 7, (short) 0, (short) 3, (short) 20, true);
        DashboardDatabase.insertOnDuplicateIgnoreDashboard(channelId, userId, DashboardType.NOTIFICATION.name(), (short) 0, (short) 0, (short) 5, (short) 10, true);
    }
}
