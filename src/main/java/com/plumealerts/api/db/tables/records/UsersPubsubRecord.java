/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables.records;


import com.plumealerts.api.db.tables.UsersPubsub;

import java.time.OffsetDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersPubsubRecord extends UpdatableRecordImpl<UsersPubsubRecord> implements Record4<String, String, OffsetDateTime, OffsetDateTime> {

    private static final long serialVersionUID = 531024885;

    /**
     * Setter for <code>public.users_pubsub.user_id</code>.
     */
    public void setUserId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.users_pubsub.user_id</code>.
     */
    public String getUserId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.users_pubsub.type</code>.
     */
    public void setType(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.users_pubsub.type</code>.
     */
    public String getType() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.users_pubsub.connected</code>.
     */
    public void setConnected(OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.users_pubsub.connected</code>.
     */
    public OffsetDateTime getConnected() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>public.users_pubsub.pong</code>.
     */
    public void setPong(OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.users_pubsub.pong</code>.
     */
    public OffsetDateTime getPong() {
        return (OffsetDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, OffsetDateTime, OffsetDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return UsersPubsub.USERS_PUBSUB.USER_ID;
    }

    @Override
    public Field<String> field2() {
        return UsersPubsub.USERS_PUBSUB.TYPE;
    }

    @Override
    public Field<OffsetDateTime> field3() {
        return UsersPubsub.USERS_PUBSUB.CONNECTED;
    }

    @Override
    public Field<OffsetDateTime> field4() {
        return UsersPubsub.USERS_PUBSUB.PONG;
    }

    @Override
    public String component1() {
        return getUserId();
    }

    @Override
    public String component2() {
        return getType();
    }

    @Override
    public OffsetDateTime component3() {
        return getConnected();
    }

    @Override
    public OffsetDateTime component4() {
        return getPong();
    }

    @Override
    public String value1() {
        return getUserId();
    }

    @Override
    public String value2() {
        return getType();
    }

    @Override
    public OffsetDateTime value3() {
        return getConnected();
    }

    @Override
    public OffsetDateTime value4() {
        return getPong();
    }

    @Override
    public UsersPubsubRecord value1(String value) {
        setUserId(value);
        return this;
    }

    @Override
    public UsersPubsubRecord value2(String value) {
        setType(value);
        return this;
    }

    @Override
    public UsersPubsubRecord value3(OffsetDateTime value) {
        setConnected(value);
        return this;
    }

    @Override
    public UsersPubsubRecord value4(OffsetDateTime value) {
        setPong(value);
        return this;
    }

    @Override
    public UsersPubsubRecord values(String value1, String value2, OffsetDateTime value3, OffsetDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UsersPubsubRecord
     */
    public UsersPubsubRecord() {
        super(UsersPubsub.USERS_PUBSUB);
    }

    /**
     * Create a detached, initialised UsersPubsubRecord
     */
    public UsersPubsubRecord(String userId, String type, OffsetDateTime connected, OffsetDateTime pong) {
        super(UsersPubsub.USERS_PUBSUB);

        set(0, userId);
        set(1, type);
        set(2, connected);
        set(3, pong);
    }
}
