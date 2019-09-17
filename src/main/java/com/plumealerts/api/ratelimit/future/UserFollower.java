package com.plumealerts.api.ratelimit.future;

import com.github.twitch4j.helix.domain.Follow;
import com.github.twitch4j.helix.domain.FollowList;
import com.plumealerts.api.PlumeAlertsAPI;
import com.plumealerts.api.db.tables.records.TwitchFollowersRecord;
import com.plumealerts.api.twitch.TwitchAPI;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class UserFollower implements FutureRequest {
    private final String userId;
    private final String token;
    private String cursor;

    public UserFollower(String userId, String token) {
        this.userId = userId;
        this.token = token;
        this.cursor = null;
    }

    @Override
    public void execute() {
        FollowList followList = TwitchAPI.helix().getFollowers(this.token, null, Long.valueOf(this.userId), this.cursor, 100).execute();
        List<Follow> followers = followList.getFollows();
        if (followers == null || followers.isEmpty()) {
            return;
        }

        List<TwitchFollowersRecord> userRecords = new ArrayList<>();
        for (Follow usersFollow : followers) {
            userRecords.add(new TwitchFollowersRecord(userId, usersFollow.getFromId().toString(), usersFollow.getFromName(), true, OffsetDateTime.of(usersFollow.getFollowedAt(), ZoneOffset.UTC)));
        }
        PlumeAlertsAPI.dslContext().batchInsert(userRecords).execute();

        this.cursor = followList.getPagination().getCursor();
        if (this.cursor != null) {
            PlumeAlertsAPI.request().add(this);
        }
    }
}
