/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables;


import com.plumealerts.api.db.Indexes;
import com.plumealerts.api.db.Keys;
import com.plumealerts.api.db.Public;
import com.plumealerts.api.db.tables.records.TwitchFollowersRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;


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
public class TwitchFollowers extends TableImpl<TwitchFollowersRecord> {

    private static final long serialVersionUID = -30040214;

    /**
     * The reference instance of <code>public.twitch_followers</code>
     */
    public static final TwitchFollowers TWITCH_FOLLOWERS = new TwitchFollowers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TwitchFollowersRecord> getRecordType() {
        return TwitchFollowersRecord.class;
    }

    /**
     * The column <code>public.twitch_followers.notification_id</code>.
     */
    public final TableField<TwitchFollowersRecord, Long> NOTIFICATION_ID = createField(DSL.name("notification_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.twitch_followers.follower_username</code>.
     */
    public final TableField<TwitchFollowersRecord, String> FOLLOWER_USERNAME = createField(DSL.name("follower_username"), org.jooq.impl.SQLDataType.VARCHAR(25).nullable(false), this, "");

    /**
     * Create a <code>public.twitch_followers</code> table reference
     */
    public TwitchFollowers() {
        this(DSL.name("twitch_followers"), null);
    }

    /**
     * Create an aliased <code>public.twitch_followers</code> table reference
     */
    public TwitchFollowers(String alias) {
        this(DSL.name(alias), TWITCH_FOLLOWERS);
    }

    /**
     * Create an aliased <code>public.twitch_followers</code> table reference
     */
    public TwitchFollowers(Name alias) {
        this(alias, TWITCH_FOLLOWERS);
    }

    private TwitchFollowers(Name alias, Table<TwitchFollowersRecord> aliased) {
        this(alias, aliased, null);
    }

    private TwitchFollowers(Name alias, Table<TwitchFollowersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TwitchFollowers(Table<O> child, ForeignKey<O, TwitchFollowersRecord> key) {
        super(child, key, TWITCH_FOLLOWERS);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TWITCH_FOLLOWERS_PKEY);
    }

    @Override
    public UniqueKey<TwitchFollowersRecord> getPrimaryKey() {
        return Keys.TWITCH_FOLLOWERS_PKEY;
    }

    @Override
    public List<UniqueKey<TwitchFollowersRecord>> getKeys() {
        return Arrays.<UniqueKey<TwitchFollowersRecord>>asList(Keys.TWITCH_FOLLOWERS_PKEY);
    }

    @Override
    public List<ForeignKey<TwitchFollowersRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TwitchFollowersRecord, ?>>asList(Keys.TWITCH_FOLLOWERS__TWITCH_FOLLOWERS_NOTIFICATION_ID_FKEY);
    }

    public Notification notification() {
        return new Notification(this, Keys.TWITCH_FOLLOWERS__TWITCH_FOLLOWERS_NOTIFICATION_ID_FKEY);
    }

    @Override
    public TwitchFollowers as(String alias) {
        return new TwitchFollowers(DSL.name(alias), this);
    }

    @Override
    public TwitchFollowers as(Name alias) {
        return new TwitchFollowers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TwitchFollowers rename(String name) {
        return new TwitchFollowers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TwitchFollowers rename(Name name) {
        return new TwitchFollowers(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
