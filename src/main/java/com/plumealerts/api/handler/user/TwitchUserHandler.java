package com.plumealerts.api.handler.user;

import com.plumealerts.api.Constants;
import com.plumealerts.api.db.TwitchUserAccessTokenDatabase;
import com.plumealerts.api.db.record.TwitchUserAccessTokenRecord;
import com.plumealerts.api.twitch.TwitchAPI;
import com.plumealerts.api.twitch.oauth2.domain.RefreshToken;
import com.plumealerts.api.twitch.oauth2.domain.Token;
import com.plumealerts.api.twitch.oauth2.domain.Validate;

import java.time.OffsetDateTime;

public final class TwitchUserHandler {

    private TwitchUserHandler() {
    }

    public static TwitchUserAccessTokenRecord getAccessToken(String userId) {
        return getAccessToken(userId, true);
    }

    public static TwitchUserAccessTokenRecord getAccessToken(String userId, boolean validateToken) {
        TwitchUserAccessTokenRecord token = TwitchUserAccessTokenDatabase.findAccessTokenByUserId(userId);

        if (token == null) {
            return null;
        }

        if (validateToken) {
            Validate validate = TwitchAPI.oAuth2().validate(token.getAccessToken()).execute();
            if (validate == null) {
                //TODO Probably wrong and add time buffer
                if (OffsetDateTime.now().isBefore(token.getExpiredAt())) {
                    RefreshToken refreshToken = TwitchAPI.oAuth2()
                            .refreshToken(Constants.TWITCH_CLIENT_ID, Constants.TWITCH_CLIENT_SECRET, token.getRefreshToken())
                            .execute();
                    if (refreshToken != null) {
                        return updateAccessToken(userId, refreshToken);
                    }
                }
                return null;
            }

            TwitchUserAccessTokenDatabase.updateValidated(userId);
        }
        return token;
    }


    public static TwitchUserAccessTokenRecord updateAccessToken(String userId, Token token) {
        TwitchUserAccessTokenRecord accessTokenRecord = getAccessToken(userId, false);
        if (accessTokenRecord != null) {
            TwitchAPI.oAuth2().revoke(Constants.TWITCH_CLIENT_ID, accessTokenRecord.getAccessToken()).queue();
        }

        return TwitchUserAccessTokenDatabase.update(userId, token);
    }
}
