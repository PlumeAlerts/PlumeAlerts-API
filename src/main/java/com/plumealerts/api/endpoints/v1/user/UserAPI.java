package com.plumealerts.api.endpoints.v1.user;

import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class UserAPI extends RoutingHandler {

    public UserAPI() {
        this.get("/v1/user", this::getUser);
    }

    private Domain getUser(HttpServerExchange exchange) {

        Domain domain = TokenValidator.hasError(exchange);
        if (domain != null) {
            return domain;
        }

        return ResponseUtil.successResponse(exchange, null);
    }
}
