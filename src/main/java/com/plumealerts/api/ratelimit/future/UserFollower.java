package com.plumealerts.api.ratelimit.future;

import com.github.twitch4j.helix.domain.Follow;
import com.github.twitch4j.helix.domain.FollowList;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.twitch.TwitchAPI;
import org.jooq.Record1;
import org.jooq.Result;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.plumealerts.api.db.Tables.NOTIFICATION;
import static com.plumealerts.api.db.Tables.TWITCH_FOLLOWERS;

public class UserFollower implements FutureRequest {
    private final String channelId;
    private final String token;
    private String cursor;

    public UserFollower(String userId, String token) {
        this.channelId = userId;
        this.token = token;
        this.cursor = null;
    }

    @Override
    public void execute() {
        FollowList followList = TwitchAPI.helix().getFollowers(this.token, null, this.channelId, this.cursor, 100).execute();
        List<Follow> followers = followList.getFollows();
        if (followers == null || followers.isEmpty()) {
            return;
        }

        for (Follow usersFollow : followers) {
            String followerId = usersFollow.getFromId();
            OffsetDateTime createdAt = OffsetDateTime.of(usersFollow.getFollowedAt(), ZoneOffset.UTC);

            Result<Record1<Long>> x = PlumeAlertsAPI.dslContext().insertInto(NOTIFICATION, NOTIFICATION.CHANNEL_ID, NOTIFICATION.USER_ID, NOTIFICATION.CREATED_AT, NOTIFICATION.TYPE)
                    .values(this.channelId, followerId, createdAt, "follow")
                    .returningResult(NOTIFICATION.ID)
                    .fetch();
            if (x.size() != 1) {
                //TODO FAILED
                return;
            }
            int idk = PlumeAlertsAPI.dslContext().insertInto(TWITCH_FOLLOWERS, TWITCH_FOLLOWERS.NOTIFICATION_ID, TWITCH_FOLLOWERS.FOLLOWER_USERNAME)
                    .values(x.get(0).value1(), usersFollow.getFromName())
                    .execute();
            if (idk != 1) {
                //TODO Failed
            }
        }

        this.cursor = followList.getPagination().getCursor();
        if (this.cursor != null) {
            PlumeAlertsAPI.request().add(this);
        }
    }
}
