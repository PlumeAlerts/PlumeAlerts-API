package com.plumealerts.api.utils;

import com.plumealerts.api.db.tables.records.UserAccessTokenRecord;
import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.handler.DataError;
import com.plumealerts.api.handler.user.HandlerUserAccessTokens;
import io.undertow.server.HttpServerExchange;

import java.time.OffsetDateTime;

public class TokenValidator {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public static DataError<String> getUserIdFromAccessToken(HttpServerExchange exchange) {
        String bearerToken = TokenValidator.getAuthorizationToken(exchange);
        if (bearerToken == null || !TokenValidator.isBearerToken(bearerToken)) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
        UserAccessTokenRecord userAccessTokenRecord = HandlerUserAccessTokens.getAccessToken(getToken(bearerToken));
        if (userAccessTokenRecord == null) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
        if (OffsetDateTime.now().isAfter(userAccessTokenRecord.getExpiredAt())) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }

        return new DataError<>(userAccessTokenRecord.getUserId());
    }

    public static DataError<String> getUserIdFromRefreshToken(HttpServerExchange exchange) {
        String bearerToken = TokenValidator.getAuthorizationToken(exchange);
        if (bearerToken == null || !TokenValidator.isBearerToken(bearerToken)) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
        UserAccessTokenRecord userAccessTokenRecord = HandlerUserAccessTokens.getRefreshToken(getToken(bearerToken));
        if (userAccessTokenRecord == null) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
        if (OffsetDateTime.now().isAfter(userAccessTokenRecord.getRefreshExpiredAt())) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }

        return new DataError<>(userAccessTokenRecord.getUserId());
    }

    public static String getToken(String bearerToken) {
        return bearerToken.substring(BEARER.length());
    }

    public static String getAuthorizationToken(HttpServerExchange exchange) {
        return exchange.getRequestHeaders().getFirst(TokenValidator.AUTHORIZATION);
    }

    public static boolean isBearerToken(String token) {
        return BEARER.regionMatches(true, 0, token, 0, BEARER.length());
    }
}
