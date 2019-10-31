package com.plumealerts.api.db;

import com.plumealerts.api.PlumeAlertsAPI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ScopeDatabase {

    private static final String FIND_SCOPES = "SELECT * FROM scopes";

    private ScopeDatabase() {
    }

    public static List<String> getScopes() {
        List<String> scopes = new ArrayList<>();

        try (PreparedStatement stmt = PlumeAlertsAPI.connection().prepareStatement(FIND_SCOPES)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    scopes.add(rs.getString("scope"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scopes;
    }
}
