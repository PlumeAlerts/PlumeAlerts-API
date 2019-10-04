/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables.records;


import com.plumealerts.api.db.tables.TwitchFollowers;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.12.1"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class TwitchFollowersRecord extends UpdatableRecordImpl<TwitchFollowersRecord> implements Record2<Long, String> {

    private static final long serialVersionUID = 1916306874;

    /**
     * Setter for <code>public.twitch_followers.notification_id</code>.
     */
    public void setNotificationId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.twitch_followers.notification_id</code>.
     */
    public Long getNotificationId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.twitch_followers.follower_username</code>.
     */
    public void setFollowerUsername(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.twitch_followers.follower_username</code>.
     */
    public String getFollowerUsername() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return TwitchFollowers.TWITCH_FOLLOWERS.NOTIFICATION_ID;
    }

    @Override
    public Field<String> field2() {
        return TwitchFollowers.TWITCH_FOLLOWERS.FOLLOWER_USERNAME;
    }

    @Override
    public Long component1() {
        return getNotificationId();
    }

    @Override
    public String component2() {
        return getFollowerUsername();
    }

    @Override
    public Long value1() {
        return getNotificationId();
    }

    @Override
    public String value2() {
        return getFollowerUsername();
    }

    @Override
    public TwitchFollowersRecord value1(Long value) {
        setNotificationId(value);
        return this;
    }

    @Override
    public TwitchFollowersRecord value2(String value) {
        setFollowerUsername(value);
        return this;
    }

    @Override
    public TwitchFollowersRecord values(Long value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TwitchFollowersRecord
     */
    public TwitchFollowersRecord() {
        super(TwitchFollowers.TWITCH_FOLLOWERS);
    }

    /**
     * Create a detached, initialised TwitchFollowersRecord
     */
    public TwitchFollowersRecord(Long notificationId, String followerUsername) {
        super(TwitchFollowers.TWITCH_FOLLOWERS);

        set(0, notificationId);
        set(1, followerUsername);
    }
}
