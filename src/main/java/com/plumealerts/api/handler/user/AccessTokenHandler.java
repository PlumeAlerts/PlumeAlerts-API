package com.plumealerts.api.handler.user;

import com.plumealerts.api.endpoints.v1.auth.domain.AccessToken;
import com.plumealerts.api.handler.user.jwt.JWT;
import com.plumealerts.api.handler.user.jwt.TokenType;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


public class AccessTokenHandler {

    private AccessTokenHandler() {
    }

    public static AccessToken generateTokens(String userId) throws JoseException {
        NumericDate expiredAt = NumericDate.fromSeconds(Instant.now().plus(60, ChronoUnit.MINUTES).getEpochSecond());
        NumericDate refreshExpiredAt = NumericDate.fromSeconds(Instant.now().plus(7, ChronoUnit.DAYS).getEpochSecond());

        return generateTokens(userId, expiredAt, refreshExpiredAt);
    }

    public static AccessToken generateTokens(String userId, NumericDate expiredAt, NumericDate refreshExpiredAt) throws JoseException {
        String accessToken = new JWT(userId, TokenType.ACCESS_TOKEN, expiredAt).generate();
        String refreshToken = new JWT(userId, TokenType.REFRESH_TOKEN, refreshExpiredAt).generate();

        return new AccessToken(accessToken, refreshToken, expiredAt.getValue(), refreshExpiredAt.getValue());
    }

    public static JwtClaims getAccessToken(String token) throws InvalidJwtException {
        return JWT.decrypt(token);
    }
}
