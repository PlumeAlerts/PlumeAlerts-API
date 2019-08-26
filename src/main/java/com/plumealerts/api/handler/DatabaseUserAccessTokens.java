package com.plumealerts.api.handler;

import com.plumealerts.api.Constants;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.UserAccessTokenRecord;
import com.plumealerts.api.twitch.TwitchAPI;
import com.plumealerts.api.twitch.oauth2.domain.RefreshToken;
import com.plumealerts.api.twitch.oauth2.domain.Token;
import com.plumealerts.api.twitch.oauth2.domain.Validate;

import java.sql.Timestamp;

import static com.plumealerts.api.db.Tables.USERS;
import static com.plumealerts.api.db.Tables.USER_ACCESS_TOKEN;

public class DatabaseUserAccessTokens {

    public static boolean setAccessToken(String userId, Token token) {
        int i = PlumeAlertsAPI.dslContext().insertInto(USER_ACCESS_TOKEN, USER_ACCESS_TOKEN.USER_ID, USER_ACCESS_TOKEN.ACCESS_TOKEN, USER_ACCESS_TOKEN.REFRESH_TOKEN, USER_ACCESS_TOKEN.EXPIRED_AT)
                .values(userId, token.getAccessToken(), token.getRefreshToken(), token.getExpire())
                .execute();
        return i == 1;
    }

    public static UserAccessTokenRecord getAccessToken(String userId) {
        return getAccessToken(userId, true);
    }

    public static UserAccessTokenRecord getAccessToken(String userId, boolean validateToken) {
        UserAccessTokenRecord accessTokenRecord = PlumeAlertsAPI.dslContext().selectFrom(USER_ACCESS_TOKEN).where(USER_ACCESS_TOKEN.USER_ID.eq(userId)).fetchOne();

        if (validateToken) {
            Validate validate = null;
            try {
                validate = TwitchAPI.oAuth2().validate(accessTokenRecord.getAccessToken()).execute();
            } catch (UnsupportedOperationException e) {

            }
            if (validate == null) {
                //TODO Probably wrong and add buffer
                if (accessTokenRecord.getExpiredAt().getTime() <= System.currentTimeMillis()) {
                    RefreshToken refreshToken = TwitchAPI.oAuth2().refresh(Constants.TWITCH_CLIENT_ID, Constants.TWITCH_CLIENT_SECRET, accessTokenRecord.getRefreshToken()).execute();
                    if (refreshToken != null) {
                        return updateAccessToken(userId, refreshToken, false);
                    }
                }
                PlumeAlertsAPI.dslContext().update(USERS)
                        .set(USERS.REFRESH_LOGIN, true)
                        .where(USERS.ID.eq(userId))
                        .executeAsync();
                return null;
            }

            PlumeAlertsAPI.dslContext().update(USER_ACCESS_TOKEN)
                    .set(USER_ACCESS_TOKEN.LAST_VALIDATED, new Timestamp(System.currentTimeMillis()))
                    .where(USER_ACCESS_TOKEN.USER_ID.eq(userId))
                    .executeAsync();
        }
        return accessTokenRecord;
    }

    public static UserAccessTokenRecord updateAccessToken(String userId, Token token) {
        return updateAccessToken(userId, token, true);
    }

    public static UserAccessTokenRecord updateAccessToken(String userId, Token token, boolean revoke) {
        if (revoke) {
            UserAccessTokenRecord accessTokenRecord = getAccessToken(userId, false);
            if (accessTokenRecord != null)
                TwitchAPI.oAuth2().revoke(Constants.TWITCH_CLIENT_ID, accessTokenRecord.getAccessToken());
        }

        return PlumeAlertsAPI.dslContext().update(USER_ACCESS_TOKEN)
                .set(USER_ACCESS_TOKEN.ACCESS_TOKEN, token.getAccessToken())
                .set(USER_ACCESS_TOKEN.REFRESH_TOKEN, token.getRefreshToken())
                .set(USER_ACCESS_TOKEN.EXPIRED_AT, token.getExpire())
                .set(USER_ACCESS_TOKEN.LAST_VALIDATED, new Timestamp(System.currentTimeMillis()))
                .where(USER_ACCESS_TOKEN.USER_ID.eq(userId))
                .returning()
                .fetchOne();
    }
}
