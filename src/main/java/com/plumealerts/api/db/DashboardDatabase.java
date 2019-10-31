package com.plumealerts.api.db;

import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.record.DashboardRecord;
import com.plumealerts.api.endpoints.v1.user.domain.DashboardDomain;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DashboardDatabase {

    private static final String FIND_BY_CHANNNELID_AND_USERID = "SELECT * FROM dashboard WHERE channel_id=? AND user_id=?";
    private static final String INSERT_DASHBOARD = "INSERT INTO dashboard(channel_id, user_id, type, x, y, width, height, show) VALUES (?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING ";
    private static final String UPDATE_DASHBOARD = "UPDATE dashboard SET x=?, y=?, width=?, height=?, show=? WHERE channel_id=? AND user_id=? AND type=?";

    private DashboardDatabase() {
    }

    public static List<DashboardRecord> findByChannelIdAndUserId(String channelId, String userId) {
        List<DashboardRecord> dashboardRecords = new ArrayList<>();
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_BY_CHANNNELID_AND_USERID)) {
            stmt.setString(1, channelId);
            stmt.setString(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dashboardRecords.add(new DashboardRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dashboardRecords;
    }

    public static boolean insertOnDuplicateIgnoreDashboard(String channelId, String userId, String type, short x, short y, short width, short height, boolean show) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(INSERT_DASHBOARD)) {
            stmt.setString(1, channelId);
            stmt.setString(2, userId);
            stmt.setString(3, type);
            stmt.setShort(4, x);
            stmt.setShort(5, y);
            stmt.setShort(6, width);
            stmt.setShort(7, height);
            stmt.setBoolean(8, show);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateDashboard(String channelId, String userId, DashboardDomain dashboard) {
        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(UPDATE_DASHBOARD)) {
            stmt.setShort(1, dashboard.getX());
            stmt.setShort(2, dashboard.getY());
            stmt.setShort(3, dashboard.getWidth());
            stmt.setShort(4, dashboard.getHeight());
            stmt.setBoolean(5, dashboard.isShow());
            stmt.setString(6, channelId);
            stmt.setString(7, userId);
            stmt.setString(8, dashboard.getType());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
