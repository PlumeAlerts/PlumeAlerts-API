package com.plumealerts.api.endpoints.v1.user;

import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.handler.user.DataError;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class UserAPI extends RoutingHandler {

    public UserAPI() {
        this.get("/v1/user", this::getUser);
    }

    private Domain getUser(HttpServerExchange exchange) {
        DataError dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }

        return ResponseUtil.successResponse(exchange, null);
    }
}
