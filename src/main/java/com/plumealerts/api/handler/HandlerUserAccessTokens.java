package com.plumealerts.api.handler;

import com.plumealerts.api.PlumeAlertsAPI;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static com.plumealerts.api.db.Tables.USER_ACCESS_TOKEN;

public class HandlerUserAccessTokens {

    public static String getToken(String userId, NumericDate numericDate) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("PlumeAlerts");
        claims.setAudience(userId);
        claims.setExpirationTime(numericDate);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setClaim("user_id", userId);

        return getToken(claims);
    }

    public static String getToken(JwtClaims claims) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(claims.toJson());
//        jws.setKey(privateKey);
//        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);
        jws.setAlgorithmConstraints(AlgorithmConstraints.NO_CONSTRAINTS);//TODO remove
        jws.setDoKeyValidation(false); //TODO remove
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.NONE); //TODO remove

        return jws.getCompactSerialization();
    }

    public static boolean insertToken(String userId, String accessToken, String refreshToken, NumericDate expiredAt, NumericDate refreshExpiredAt) {
        OffsetDateTime expiredAtOffset = OffsetDateTime.ofInstant(Instant.ofEpochSecond(expiredAt.getValue()), ZoneId.systemDefault());
        OffsetDateTime refreshExpiredAtOffset = OffsetDateTime.ofInstant(Instant.ofEpochSecond(refreshExpiredAt.getValue()), ZoneId.systemDefault());

        int i = PlumeAlertsAPI.dslContext().insertInto(USER_ACCESS_TOKEN, USER_ACCESS_TOKEN.USER_ID, USER_ACCESS_TOKEN.ACCESS_TOKEN, USER_ACCESS_TOKEN.REFRESH_TOKEN, USER_ACCESS_TOKEN.EXPIRED_AT, USER_ACCESS_TOKEN.REFRESH_EXPIRED_AT)
                .values(userId, accessToken, refreshToken, expiredAtOffset, refreshExpiredAtOffset)
                .execute();
        return i == 1;
    }
}
