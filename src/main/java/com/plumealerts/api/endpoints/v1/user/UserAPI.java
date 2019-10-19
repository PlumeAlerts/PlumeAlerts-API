package com.plumealerts.api.endpoints.v1.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plumealerts.api.db.tables.records.DashboardRecord;
import com.plumealerts.api.db.tables.records.NotificationRecord;
import com.plumealerts.api.db.tables.records.TwitchFollowersRecord;
import com.plumealerts.api.db.tables.records.UsersRecord;
import com.plumealerts.api.endpoints.v1.domain.Data;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.endpoints.v1.user.domain.Dashboard;
import com.plumealerts.api.endpoints.v1.user.domain.User;
import com.plumealerts.api.endpoints.v1.user.domain.notification.NotificationData;
import com.plumealerts.api.endpoints.v1.user.domain.notification.NotificationFollow;
import com.plumealerts.api.handler.DataError;
import com.plumealerts.api.handler.db.DatabaseUser;
import com.plumealerts.api.handler.user.DashboardType;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.plumealerts.api.PlumeAlertsAPI.MAPPER;

public class UserAPI extends RoutingHandler {

    public UserAPI() {
        this.get("/v1/user", this::getUser);
        this.get("/v1/user/dashboard", this::getDashboard);
        this.put("/v1/user/dashboard", this::putDashboard);
        this.get("/v1/user/notifications", this::getUserNotifications);
    }

    private Domain getUser(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();
        UsersRecord user = DatabaseUser.findUser(userId);
        if (user == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, null);
        }
        return ResponseUtil.successResponse(exchange, new User(user.getId(), user.getLogin(), user.getDisplayName(), user.getBeta(), user.getBroadcasterType()));
    }

    private Domain getDashboard(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();

        List<DashboardRecord> dashboards = DatabaseUser.findUserDashboard(userId);
        List<Dashboard> data = new ArrayList<>();

        for (DashboardRecord dashboard : dashboards) {
            data.add(new Dashboard(dashboard.getType(), dashboard.getX(), dashboard.getY(), dashboard.getWidth(), dashboard.getHeight(), dashboard.getShow()));
        }
        return ResponseUtil.successResponse(exchange, data);
    }

    private Domain putDashboard(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();

        try {
            Data<Dashboard> data = MAPPER.readValue(exchange.getInputStream(), new TypeReference<Data<Dashboard>>() {
            });
            Dashboard dashboard = data.getData();
            DashboardType type = DashboardType.valueOf(dashboard.getType());
            if (!DatabaseUser.updateDashboard(userId, type, dashboard)) {
                return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "DB Issue");
            }
            return ResponseUtil.successResponse(exchange, dashboard);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Body is invalid");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "");
        }
    }

    private Domain getUserNotifications(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        List<NotificationRecord> notifications = DatabaseUser.findUserNotifications(dataError.getData());
        List<NotificationData> data = new ArrayList<>();

        for (NotificationRecord notification : notifications) {
            if (notification.getType().equalsIgnoreCase("follow")) {
                TwitchFollowersRecord follow = DatabaseUser.findFollowNotifications(notification.getId());
                data.add(new NotificationFollow(notification.getUserId(), follow.getFollowerUsername(), notification.getCreatedAt().toEpochSecond(), notification.getType()));
            }
        }
        return ResponseUtil.successResponse(exchange, data);
    }
}