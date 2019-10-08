package com.plumealerts.api.utils;

import com.plumealerts.api.endpoints.v1.domain.error.ErrorType;
import com.plumealerts.api.handler.DataError;
import com.plumealerts.api.handler.user.HandlerUserAccessTokens;
import com.plumealerts.api.handler.user.TokenType;
import io.undertow.server.HttpServerExchange;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;

public class TokenValidator {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private TokenValidator() {
    }

    public static DataError<String> getUserIdFromAccessToken(HttpServerExchange exchange) {
        String bearerToken = TokenValidator.getAuthorizationToken(exchange);
        if (bearerToken == null || !TokenValidator.isBearerToken(bearerToken)) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
        try {
            JwtClaims claims = HandlerUserAccessTokens.getAccessToken(getToken(bearerToken));
            if (claims == null) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
            }
            if (!claims.hasClaim("type")) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
            }
            TokenType tokenType = TokenType.valueOf(claims.getStringClaimValue("type"));
            if (!TokenType.ACCESS_TOKEN.equals(tokenType)) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
            }
            return new DataError<>(claims.getSubject());
        } catch (InvalidJwtException e) {
            if (e.hasExpired()) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "Expired"));
            }
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        } catch (MalformedClaimException e) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
    }

    public static DataError<String> getUserIdFromRefreshToken(HttpServerExchange exchange) {
        String bearerToken = TokenValidator.getAuthorizationToken(exchange);
        if (bearerToken == null || !TokenValidator.isBearerToken(bearerToken)) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
        try {
            JwtClaims claims = HandlerUserAccessTokens.getAccessToken(getToken(bearerToken));
            if (claims == null) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
            }
            if (!claims.hasClaim("type")) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
            }
            TokenType tokenType = TokenType.valueOf(claims.getStringClaimValue("type"));
            if (!TokenType.REFRESH_TOKEN.equals(tokenType)) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
            }
            return new DataError<>(claims.getSubject());
        } catch (InvalidJwtException e) {
            if (e.hasExpired()) {
                return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, "Expired"));
            }
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        } catch (MalformedClaimException e) {
            return DataError.error(ResponseUtil.errorResponse(exchange, ErrorType.UNAUTHORIZED, ""));
        }
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
