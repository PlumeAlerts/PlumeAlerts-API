/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables.records;


import com.plumealerts.api.db.tables.UserPermission;
import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.12.2"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class UserPermissionRecord extends UpdatableRecordImpl<UserPermissionRecord> implements Record3<String, String, String> {

    private static final long serialVersionUID = -639804111;

    /**
     * Setter for <code>public.user_permission.channel_id</code>.
     */
    public void setChannelId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.user_permission.channel_id</code>.
     */
    public String getChannelId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.user_permission.user_id</code>.
     */
    public void setUserId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.user_permission.user_id</code>.
     */
    public String getUserId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.user_permission.permission</code>.
     */
    public void setPermission(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.user_permission.permission</code>.
     */
    public String getPermission() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record3<String, String, String> key() {
        return (Record3) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return UserPermission.USER_PERMISSION.CHANNEL_ID;
    }

    @Override
    public Field<String> field2() {
        return UserPermission.USER_PERMISSION.USER_ID;
    }

    @Override
    public Field<String> field3() {
        return UserPermission.USER_PERMISSION.PERMISSION;
    }

    @Override
    public String component1() {
        return getChannelId();
    }

    @Override
    public String component2() {
        return getUserId();
    }

    @Override
    public String component3() {
        return getPermission();
    }

    @Override
    public String value1() {
        return getChannelId();
    }

    @Override
    public String value2() {
        return getUserId();
    }

    @Override
    public String value3() {
        return getPermission();
    }

    @Override
    public UserPermissionRecord value1(String value) {
        setChannelId(value);
        return this;
    }

    @Override
    public UserPermissionRecord value2(String value) {
        setUserId(value);
        return this;
    }

    @Override
    public UserPermissionRecord value3(String value) {
        setPermission(value);
        return this;
    }

    @Override
    public UserPermissionRecord values(String value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserPermissionRecord
     */
    public UserPermissionRecord() {
        super(UserPermission.USER_PERMISSION);
    }

    /**
     * Create a detached, initialised UserPermissionRecord
     */
    public UserPermissionRecord(String channelId, String userId, String permission) {
        super(UserPermission.USER_PERMISSION);

        set(0, channelId);
        set(1, userId);
        set(2, permission);
    }
}
