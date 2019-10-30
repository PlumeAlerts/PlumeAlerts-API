/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables;


import com.plumealerts.api.db.Indexes;
import com.plumealerts.api.db.Keys;
import com.plumealerts.api.db.Public;
import com.plumealerts.api.db.tables.records.UserPermissionPendingRecord;
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
                "jOOQ version:3.12.2"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class UserPermissionPending extends TableImpl<UserPermissionPendingRecord> {

    private static final long serialVersionUID = 665404293;

    /**
     * The reference instance of <code>public.user_permission_pending</code>
     */
    public static final UserPermissionPending USER_PERMISSION_PENDING = new UserPermissionPending();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserPermissionPendingRecord> getRecordType() {
        return UserPermissionPendingRecord.class;
    }

    /**
     * The column <code>public.user_permission_pending.channel_id</code>.
     */
    public final TableField<UserPermissionPendingRecord, String> CHANNEL_ID = createField(DSL.name("channel_id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.user_permission_pending.user_id</code>.
     */
    public final TableField<UserPermissionPendingRecord, String> USER_ID = createField(DSL.name("user_id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.user_permission_pending.permission</code>.
     */
    public final TableField<UserPermissionPendingRecord, String> PERMISSION = createField(DSL.name("permission"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * Create a <code>public.user_permission_pending</code> table reference
     */
    public UserPermissionPending() {
        this(DSL.name("user_permission_pending"), null);
    }

    /**
     * Create an aliased <code>public.user_permission_pending</code> table reference
     */
    public UserPermissionPending(String alias) {
        this(DSL.name(alias), USER_PERMISSION_PENDING);
    }

    /**
     * Create an aliased <code>public.user_permission_pending</code> table reference
     */
    public UserPermissionPending(Name alias) {
        this(alias, USER_PERMISSION_PENDING);
    }

    private UserPermissionPending(Name alias, Table<UserPermissionPendingRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserPermissionPending(Name alias, Table<UserPermissionPendingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> UserPermissionPending(Table<O> child, ForeignKey<O, UserPermissionPendingRecord> key) {
        super(child, key, USER_PERMISSION_PENDING);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.USER_PERMISSION_PENDING_PKEY);
    }

    @Override
    public UniqueKey<UserPermissionPendingRecord> getPrimaryKey() {
        return Keys.USER_PERMISSION_PENDING_PKEY;
    }

    @Override
    public List<UniqueKey<UserPermissionPendingRecord>> getKeys() {
        return Arrays.<UniqueKey<UserPermissionPendingRecord>>asList(Keys.USER_PERMISSION_PENDING_PKEY);
    }

    @Override
    public List<ForeignKey<UserPermissionPendingRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<UserPermissionPendingRecord, ?>>asList(Keys.USER_PERMISSION_PENDING__USER_PERMISSION_PENDING_CHANNEL_ID_FKEY);
    }

    public Users users() {
        return new Users(this, Keys.USER_PERMISSION_PENDING__USER_PERMISSION_PENDING_CHANNEL_ID_FKEY);
    }

    @Override
    public UserPermissionPending as(String alias) {
        return new UserPermissionPending(DSL.name(alias), this);
    }

    @Override
    public UserPermissionPending as(Name alias) {
        return new UserPermissionPending(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserPermissionPending rename(String name) {
        return new UserPermissionPending(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserPermissionPending rename(Name name) {
        return new UserPermissionPending(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
