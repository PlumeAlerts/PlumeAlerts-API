package com.plumealerts.api.utils;

import com.plumealerts.api.db.tables.records.UserAccessTokenRecord;
import com.plumealerts.api.endpoints.v1.domain.Domain;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.handler.HandlerUserAccessTokens;
import io.undertow.server.HttpServerExchange;

import java.time.OffsetDateTime;

public class TokenValidator {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    public static Domain hasError(HttpServerExchange exchange) {
        String bearerToken = exchange.getRequestHeaders().getFirst(AUTHORIZATION);
        if (bearerToken == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "");
        }
        if (!TokenValidator.isBearerToken(bearerToken)) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "");
        }

        String token = bearerToken.substring(BEARER.length());
        UserAccessTokenRecord userAccessTokenRecord = HandlerUserAccessTokens.getToken(token);
        if (userAccessTokenRecord == null) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "");
        }
        if (OffsetDateTime.now().isAfter(userAccessTokenRecord.getExpiredAt())) {
            return ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "");
        }
        return null;
    }

    private static boolean isBearerToken(String token) {
        return BEARER.regionMatches(true, 0, token, 0, BEARER.length());
    }
}
