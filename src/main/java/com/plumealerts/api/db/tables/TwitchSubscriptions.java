/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables;


import com.plumealerts.api.db.Indexes;
import com.plumealerts.api.db.Keys;
import com.plumealerts.api.db.Public;
import com.plumealerts.api.db.tables.records.TwitchSubscriptionsRecord;
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
public class TwitchSubscriptions extends TableImpl<TwitchSubscriptionsRecord> {

    private static final long serialVersionUID = -1745668685;

    /**
     * The reference instance of <code>public.twitch_subscriptions</code>
     */
    public static final TwitchSubscriptions TWITCH_SUBSCRIPTIONS = new TwitchSubscriptions();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TwitchSubscriptionsRecord> getRecordType() {
        return TwitchSubscriptionsRecord.class;
    }

    /**
     * The column <code>public.twitch_subscriptions.notification_id</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, Long> NOTIFICATION_ID = createField(DSL.name("notification_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.twitch_subscriptions.recipient_username</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, String> RECIPIENT_USERNAME = createField(DSL.name("recipient_username"), org.jooq.impl.SQLDataType.VARCHAR(25).nullable(false), this, "");

    /**
     * The column <code>public.twitch_subscriptions.recipient_display_name</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, String> RECIPIENT_DISPLAY_NAME = createField(DSL.name("recipient_display_name"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.twitch_subscriptions.sub_plan</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, String> SUB_PLAN = createField(DSL.name("sub_plan"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.twitch_subscriptions.cumulative_months</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, Integer> CUMULATIVE_MONTHS = createField(DSL.name("cumulative_months"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.twitch_subscriptions.streak_months</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, Integer> STREAK_MONTHS = createField(DSL.name("streak_months"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.twitch_subscriptions.context</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, String> CONTEXT = createField(DSL.name("context"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.twitch_subscriptions.message</code>.
     */
    public final TableField<TwitchSubscriptionsRecord, String> MESSAGE = createField(DSL.name("message"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * Create a <code>public.twitch_subscriptions</code> table reference
     */
    public TwitchSubscriptions() {
        this(DSL.name("twitch_subscriptions"), null);
    }

    /**
     * Create an aliased <code>public.twitch_subscriptions</code> table reference
     */
    public TwitchSubscriptions(String alias) {
        this(DSL.name(alias), TWITCH_SUBSCRIPTIONS);
    }

    /**
     * Create an aliased <code>public.twitch_subscriptions</code> table reference
     */
    public TwitchSubscriptions(Name alias) {
        this(alias, TWITCH_SUBSCRIPTIONS);
    }

    private TwitchSubscriptions(Name alias, Table<TwitchSubscriptionsRecord> aliased) {
        this(alias, aliased, null);
    }

    private TwitchSubscriptions(Name alias, Table<TwitchSubscriptionsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TwitchSubscriptions(Table<O> child, ForeignKey<O, TwitchSubscriptionsRecord> key) {
        super(child, key, TWITCH_SUBSCRIPTIONS);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TWITCH_SUBSCRIPTIONS_PKEY);
    }

    @Override
    public UniqueKey<TwitchSubscriptionsRecord> getPrimaryKey() {
        return Keys.TWITCH_SUBSCRIPTIONS_PKEY;
    }

    @Override
    public List<UniqueKey<TwitchSubscriptionsRecord>> getKeys() {
        return Arrays.<UniqueKey<TwitchSubscriptionsRecord>>asList(Keys.TWITCH_SUBSCRIPTIONS_PKEY);
    }

    @Override
    public List<ForeignKey<TwitchSubscriptionsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TwitchSubscriptionsRecord, ?>>asList(Keys.TWITCH_SUBSCRIPTIONS__TWITCH_SUBSCRIPTIONS_NOTIFICATION_ID_FKEY);
    }

    public Notification notification() {
        return new Notification(this, Keys.TWITCH_SUBSCRIPTIONS__TWITCH_SUBSCRIPTIONS_NOTIFICATION_ID_FKEY);
    }

    @Override
    public TwitchSubscriptions as(String alias) {
        return new TwitchSubscriptions(DSL.name(alias), this);
    }

    @Override
    public TwitchSubscriptions as(Name alias) {
        return new TwitchSubscriptions(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TwitchSubscriptions rename(String name) {
        return new TwitchSubscriptions(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TwitchSubscriptions rename(Name name) {
        return new TwitchSubscriptions(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, String, String, String, Integer, Integer, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}
