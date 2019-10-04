package com.plumealerts.api.endpoints.v1.user;

import com.plumealerts.api.db.tables.records.UsersRecord;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.endpoints.v1.user.domain.User;
import com.plumealerts.api.handler.DataError;
import com.plumealerts.api.handler.HandlerUser;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class UserAPI extends RoutingHandler {

    public UserAPI() {
        this.get("/v1/user", this::getUser);
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
}
