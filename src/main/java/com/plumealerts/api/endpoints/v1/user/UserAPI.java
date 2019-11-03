package com.plumealerts.api.endpoints.v1.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plumealerts.api.db.DashboardDatabase;
import com.plumealerts.api.db.NotificationDatabase;
import com.plumealerts.api.db.UserDatabase;
import com.plumealerts.api.db.record.*;
import com.plumealerts.api.endpoints.v1.domain.DataDomain;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.endpoints.v1.user.domain.DashboardDomain;
import com.plumealerts.api.endpoints.v1.user.domain.UserDomain;
import com.plumealerts.api.endpoints.v1.user.domain.notification.*;
import com.plumealerts.api.handler.DataError;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import com.plumealerts.api.utils.permission.Permission;
import com.plumealerts.api.utils.permission.PermissionUtil;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.plumealerts.api.PlumeAlertsAPI.MAPPER;

public class UserAPI extends RoutingHandler {

    private static final Logger LOGGER = Logger.getLogger(UserAPI.class.getName());

    public UserAPI() {
        this.get("/v1/user/{username}", this::getUser);
        this.get("/v1/user/{username}/dashboard", this::getDashboard);
        this.put("/v1/user/{username}/dashboard", this::putDashboard);
        this.get("/v1/user/{username}/notifications", this::getNotification);
        this.put("/v1/user/{username}/notifications", this::putNotification);
    }

    // TODO When adding user permissions add default dashboard

    private Domain getUser(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();
        String channelId = PermissionUtil.getChannelId(exchange, userId);
        if (channelId == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Channel not found.");
        }
        if (!PermissionUtil.hasPermission(channelId, userId, Permission.USER)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "Don't have permission: " + Permission.USER.getName());
        }
        UserRecord user = UserDatabase.findById(channelId);
        if (user == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, null);
        }
        return ResponseUtil.successResponse(exchange, new UserDomain(user.getId(), user.getLogin(), user.getDisplayName(), user.isBeta(), user.getBroadcasterType()));
    }

    private Domain getDashboard(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();
        String channelId = PermissionUtil.getChannelId(exchange, userId);
        if (channelId == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Channel not found.");
        }
        if (!PermissionUtil.hasPermission(channelId, userId, Permission.USER)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "Don't have permission: " + Permission.USER.getName());
        }
        List<DashboardRecord> dashboards = DashboardDatabase.findByChannelIdAndUserId(channelId, userId);
        List<DashboardDomain> data = new ArrayList<>();

        for (DashboardRecord dashboard : dashboards) {
            data.add(new DashboardDomain(dashboard.getType(), dashboard.getX(), dashboard.getY(), dashboard.getWidth(), dashboard.getHeight(), dashboard.isShow()));
        }
        return ResponseUtil.successResponse(exchange, data);
    }

    private Domain putDashboard(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();
        String channelId = PermissionUtil.getChannelId(exchange, userId);
        if (channelId == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Channel not found.");
        }
        if (!PermissionUtil.hasPermission(channelId, userId, Permission.USER)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "Don't have permission: " + Permission.USER.getName());
        }
        try {
            DataDomain<DashboardDomain> data = MAPPER.readValue(exchange.getInputStream(), new TypeReference<DataDomain<DashboardDomain>>() {
            });
            DashboardDomain dashboard = data.getData();
            if (!DashboardDatabase.updateDashboard(channelId, userId, dashboard)) {
                return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "DB Issue");
            }
            return ResponseUtil.successResponse(exchange, dashboard);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Invalid body", e);
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Body is invalid");
        }
    }

    private Domain getNotification(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }

        String userId = dataError.getData();
        String channelId = PermissionUtil.getChannelId(exchange, userId);
        if (channelId == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Channel not found.");
        }
        if (!PermissionUtil.hasPermission(channelId, userId, Permission.NOTIFICATION_VIEW)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "Don't have permission: " + Permission.NOTIFICATION_VIEW.getName());
        }
        List<NotificationRecord> notifications = NotificationDatabase.getNotifications(channelId, new ArrayList<>());
        List<NotificationData> data = new ArrayList<>();

        for (NotificationRecord notification : notifications) {
            if (notification instanceof NotificationBitRecord) {
                data.add(new NotificationBit((NotificationBitRecord) notification));
            } else if (notification instanceof NotificationFollowRecord) {
                data.add(new NotificationFollow((NotificationFollowRecord) notification));
            } else if (notification instanceof NotificationGiftSubscriptionRecord) {
                data.add(new NotificationGiftSubscription((NotificationGiftSubscriptionRecord) notification));
            } else if (notification instanceof NotificationSubscriptionRecord) {
                data.add(new NotificationSubscription((NotificationSubscriptionRecord) notification));
            }
        }

        return ResponseUtil.successResponse(exchange, data);
    }

    private Domain putNotification(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();
        String channelId = PermissionUtil.getChannelId(exchange, userId);
        if (channelId == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Channel not found.");
        }
        if (!PermissionUtil.hasPermission(channelId, userId, Permission.NOTIFICATION_EDIT)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "Don't have permission: " + Permission.NOTIFICATION_EDIT.getName());
        }
        try {
            DataDomain<NotificationHideDomain> data = MAPPER.readValue(exchange.getInputStream(), new TypeReference<DataDomain<NotificationHideDomain>>() {
            });
            NotificationHideDomain notification = data.getData();

            if (!NotificationDatabase.updateNotification(notification.getId(), notification.isHide())) {
                return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "DB Issue");
            }
            return ResponseUtil.successResponse(exchange, notification);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Invalid body", e);
            return ResponseUtil.errorResponse(exchange, ErrorType.BAD_REQUEST, "Body is invalid");
        }
    }
}