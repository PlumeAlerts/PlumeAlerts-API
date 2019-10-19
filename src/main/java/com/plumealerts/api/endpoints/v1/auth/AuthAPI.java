package com.plumealerts.api.endpoints.v1.auth;

import com.plumealerts.api.endpoints.v1.auth.domain.AccessToken;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.handler.DataError;
import com.plumealerts.api.handler.user.AccessTokenHandler;
import com.plumealerts.api.utils.ResponseUtil;
import com.plumealerts.api.utils.TokenValidator;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import org.jose4j.lang.JoseException;

public class AuthAPI extends RoutingHandler {

    public AuthAPI() {
        this.post("/v1/auth/validate", this::postValidate);
        this.post("/v1/auth/refresh", this::postRefresh);
    }

    private Domain postValidate(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromAccessToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        return ResponseUtil.successResponse(exchange, null);
    }

    private Domain postRefresh(HttpServerExchange exchange) {
        DataError<String> dataError = TokenValidator.getUserIdFromRefreshToken(exchange);
        if (dataError.hasError()) {
            return dataError.getError();
        }
        AccessToken accessToken;
        try {
            accessToken = AccessTokenHandler.generateTokens(dataError.getData());
        } catch (JoseException e) {
            e.printStackTrace();
            return ResponseUtil.errorResponse(exchange, ErrorType.INTERNAL_SERVER_ERROR, "");
        }

        return ResponseUtil.successResponse(exchange, accessToken);
    }
}