package com.plumealerts.api.endpoints.v1.user;

import com.plumealerts.api.db.tables.records.NotificationRecord;
import com.plumealerts.api.db.tables.records.TwitchFollowersRecord;
import com.plumealerts.api.db.tables.records.UsersRecord;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.endpoints.v1.user.domain.User;
import com.plumealerts.api.endpoints.v1.user.domain.notification.NotificationData;
import com.plumealerts.api.endpoints.v1.user.domain.notification.NotificationFollow;
import com.plumealerts.api.handler.DataError;
import com.plumealerts.api.handler.HandlerUser;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

import java.util.ArrayList;
import java.util.List;

public class UserAPI extends RoutingHandler {

    public UserAPI() {
        this.get("/v1/user", this::getUser);
        this.get("/v1/user/notifications", this::getUserNotifications);
    }

    private Domain getUser(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        String userId = dataError.getData();
        UsersRecord user = HandlerUser.findUser(userId);
        if (user == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, null);
        }
        return ResponseUtil.successResponse(exchange, new User(user.getId(), user.getLogin(), user.getDisplayName(), user.getBeta(), user.getBroadcasterType()));
    }

    private Domain getUserNotifications(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        List<NotificationRecord> notifications = HandlerUser.findUserNotifications(dataError.getData());
        List<NotificationData> data = new ArrayList<>();

        for (NotificationRecord notification : notifications) {
            if (notification.getType().equalsIgnoreCase("follow")) {
                TwitchFollowersRecord follow = HandlerUser.findFollowNotifications(notification.getId());
                data.add(new NotificationFollow(notification.getUserId(), follow.getFollowerUsername(), notification.getCreatedAt().toEpochSecond(), notification.getType()));
            }
        }
        return ResponseUtil.successResponse(exchange, data);
    }

}
