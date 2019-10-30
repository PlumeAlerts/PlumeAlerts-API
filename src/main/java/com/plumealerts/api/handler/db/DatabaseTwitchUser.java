package com.plumealerts.api.handler.db;

import com.plumealerts.api.Constants;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.TwitchUserAccessTokenRecord;
import com.plumealerts.api.twitch.TwitchAPI;
import com.plumealerts.api.twitch.oauth2.domain.RefreshToken;
import com.plumealerts.api.twitch.oauth2.domain.Token;
import com.plumealerts.api.twitch.oauth2.domain.Validate;

import java.time.OffsetDateTime;

import static com.plumealerts.api.db.Tables.TWITCH_USER_ACCESS_TOKEN;
import static com.plumealerts.api.db.Tables.USERS;

public class DatabaseTwitchUser {

    private DatabaseTwitchUser() {
    }

    public static boolean setAccessToken(String userId, Token token) {
        int i = PlumeAlertsAPI.dslContext().insertInto(TWITCH_USER_ACCESS_TOKEN, TWITCH_USER_ACCESS_TOKEN.USER_ID, TWITCH_USER_ACCESS_TOKEN.ACCESS_TOKEN, TWITCH_USER_ACCESS_TOKEN.REFRESH_TOKEN, TWITCH_USER_ACCESS_TOKEN.EXPIRED_AT)
                .values(userId, token.getAccessToken(), token.getRefreshToken(), token.getExpire())
                .execute();
        return i == 1;
    }

    public static TwitchUserAccessTokenRecord getAccessToken(String userId) {
        return getAccessToken(userId, true);
    }

    public static TwitchUserAccessTokenRecord getAccessToken(String userId, boolean validateToken) {
        TwitchUserAccessTokenRecord accessTokenRecord = PlumeAlertsAPI.dslContext().selectFrom(TWITCH_USER_ACCESS_TOKEN)
                .where(TWITCH_USER_ACCESS_TOKEN.USER_ID.eq(userId))
                .fetchOne();

        if (validateToken) {
            Validate validate;
            try {
                validate = TwitchAPI.oAuth2().validate(accessTokenRecord.getAccessToken()).execute();
            } catch (UnsupportedOperationException e) {
                //TODO Error somehow?
                return null;
            }
            if (validate == null) {
                //TODO Probably wrong and add time buffer
                if (OffsetDateTime.now().isBefore(accessTokenRecord.getExpiredAt())) {
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

            PlumeAlertsAPI.dslContext().update(TWITCH_USER_ACCESS_TOKEN)
                    .set(TWITCH_USER_ACCESS_TOKEN.LAST_VALIDATED, OffsetDateTime.now())
                    .where(TWITCH_USER_ACCESS_TOKEN.USER_ID.eq(userId))
                    .executeAsync();
        }
        return accessTokenRecord;
    }

    public static TwitchUserAccessTokenRecord updateAccessToken(String userId, Token token) {
        return updateAccessToken(userId, token, true);
    }

    public static TwitchUserAccessTokenRecord updateAccessToken(String userId, Token token, boolean revoke) {
        if (revoke) {
            TwitchUserAccessTokenRecord accessTokenRecord = getAccessToken(userId, false);
            if (accessTokenRecord != null)
                TwitchAPI.oAuth2().revoke(Constants.TWITCH_CLIENT_ID, accessTokenRecord.getAccessToken());
        }

        return PlumeAlertsAPI.dslContext().update(TWITCH_USER_ACCESS_TOKEN)
                .set(TWITCH_USER_ACCESS_TOKEN.ACCESS_TOKEN, token.getAccessToken())
                .set(TWITCH_USER_ACCESS_TOKEN.REFRESH_TOKEN, token.getRefreshToken())
                .set(TWITCH_USER_ACCESS_TOKEN.EXPIRED_AT, token.getExpire())
                .set(TWITCH_USER_ACCESS_TOKEN.LAST_VALIDATED, OffsetDateTime.now())
                .where(TWITCH_USER_ACCESS_TOKEN.USER_ID.eq(userId))
                .returning()
                .fetchOne();
    }
}
