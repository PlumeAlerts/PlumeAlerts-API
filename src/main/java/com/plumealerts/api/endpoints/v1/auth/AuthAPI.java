package com.plumealerts.api.endpoints.v1.auth;

import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class AuthAPI extends RoutingHandler {

    public AuthAPI() {
        this.post("/v1/auth/validate", this::postValidate);
    }

    private Domain postValidate(HttpServerExchange exchange) {
        Domain domain = TokenValidator.hasError(exchange);
        if (domain != null) {
            return domain;
        }

        return ResponseUtil.successResponse(exchange, null);
    }
}