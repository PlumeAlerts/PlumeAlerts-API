package com.plumealerts.api.handler.user;

import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.UserAccessTokenRecord;
import com.plumealerts.api.endpoints.v1.auth.domain.AccessToken;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static com.plumealerts.api.db.Tables.USER_ACCESS_TOKEN;

public class HandlerUserAccessTokens {

    public static AccessToken generateTokens(String userId) throws JoseException {
        NumericDate expiredAt = NumericDate.fromSeconds(Instant.now().plus(60, ChronoUnit.MINUTES).getEpochSecond());
        NumericDate refreshExpiredAt = NumericDate.fromSeconds(Instant.now().plus(7, ChronoUnit.DAYS).getEpochSecond());

        return generateTokens(userId, expiredAt, refreshExpiredAt);
    }

    public static AccessToken generateTokens(String userId, NumericDate expiredAt, NumericDate refreshExpiredAt) throws JoseException {
        String accessToken = new JWT(userId, expiredAt).generate();
        String refreshToken = new JWT(userId, refreshExpiredAt).generate();

        if (!insertToken(userId, accessToken, refreshToken, expiredAt, refreshExpiredAt)) {
            return null;
        }

        return new AccessToken(accessToken, refreshToken, expiredAt.getValue(), refreshExpiredAt.getValue());
    }

    private static boolean insertToken(String userId, String accessToken, String refreshToken, NumericDate expiredAt, NumericDate refreshExpiredAt) {
        OffsetDateTime expiredAtOffset = OffsetDateTime.ofInstant(Instant.ofEpochSecond(expiredAt.getValue()), ZoneOffset.UTC);
        OffsetDateTime refreshExpiredAtOffset = OffsetDateTime.ofInstant(Instant.ofEpochSecond(refreshExpiredAt.getValue()), ZoneOffset.UTC);

        int i = PlumeAlertsAPI.dslContext().insertInto(USER_ACCESS_TOKEN, USER_ACCESS_TOKEN.USER_ID, USER_ACCESS_TOKEN.ACCESS_TOKEN, USER_ACCESS_TOKEN.REFRESH_TOKEN, USER_ACCESS_TOKEN.EXPIRED_AT, USER_ACCESS_TOKEN.REFRESH_EXPIRED_AT)
                .values(userId, accessToken, refreshToken, expiredAtOffset, refreshExpiredAtOffset)
                .execute();
        return i == 1;
    }

    public static UserAccessTokenRecord getAccessToken(String token) {
        return PlumeAlertsAPI.dslContext().selectFrom(USER_ACCESS_TOKEN)
                .where(USER_ACCESS_TOKEN.ACCESS_TOKEN.eq(token))
                .fetchOne();
    }

    public static UserAccessTokenRecord getRefreshToken(String token) {
        return PlumeAlertsAPI.dslContext().selectFrom(USER_ACCESS_TOKEN)
                .where(USER_ACCESS_TOKEN.REFRESH_TOKEN.eq(token))
                .fetchOne();
    }
}
