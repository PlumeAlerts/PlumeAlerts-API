/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables;


import com.plumealerts.api.db.Indexes;
import com.plumealerts.api.db.Keys;
import com.plumealerts.api.db.Public;
import com.plumealerts.api.db.tables.records.UsersRecord;
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
public class Users extends TableImpl<UsersRecord> {

    private static final long serialVersionUID = 439063553;

    /**
     * The reference instance of <code>public.users</code>
     */
    public static final Users USERS = new Users();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UsersRecord> getRecordType() {
        return UsersRecord.class;
    }

    /**
     * The column <code>public.users.id</code>.
     */
    public final TableField<UsersRecord, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.users.email</code>.
     */
    public final TableField<UsersRecord, String> EMAIL = createField(DSL.name("email"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.users.login</code>.
     */
    public final TableField<UsersRecord, String> LOGIN = createField(DSL.name("login"), org.jooq.impl.SQLDataType.VARCHAR(25).nullable(false), this, "");

    /**
     * The column <code>public.users.display_name</code>.
     */
    public final TableField<UsersRecord, String> DISPLAY_NAME = createField(DSL.name("display_name"), org.jooq.impl.SQLDataType.VARCHAR(25).nullable(false), this, "");

    /**
     * The column <code>public.users.broadcaster_type</code>.
     */
    public final TableField<UsersRecord, String> BROADCASTER_TYPE = createField(DSL.name("broadcaster_type"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.users.type</code>.
     */
    public final TableField<UsersRecord, String> TYPE = createField(DSL.name("type"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.users.view_count</code>.
     */
    public final TableField<UsersRecord, Long> VIEW_COUNT = createField(DSL.name("view_count"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.users.beta</code>.
     */
    public final TableField<UsersRecord, Boolean> BETA = createField(DSL.name("beta"), org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.users.refresh_login</code>.
     */
    public final TableField<UsersRecord, Boolean> REFRESH_LOGIN = createField(DSL.name("refresh_login"), org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>public.users</code> table reference
     */
    public Users() {
        this(DSL.name("users"), null);
    }

    /**
     * Create an aliased <code>public.users</code> table reference
     */
    public Users(String alias) {
        this(DSL.name(alias), USERS);
    }

    /**
     * Create an aliased <code>public.users</code> table reference
     */
    public Users(Name alias) {
        this(alias, USERS);
    }

    private Users(Name alias, Table<UsersRecord> aliased) {
        this(alias, aliased, null);
    }

    private Users(Name alias, Table<UsersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Users(Table<O> child, ForeignKey<O, UsersRecord> key) {
        super(child, key, USERS);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.USERS_PKEY);
    }

    @Override
    public UniqueKey<UsersRecord> getPrimaryKey() {
        return Keys.USERS_PKEY;
    }

    @Override
    public List<UniqueKey<UsersRecord>> getKeys() {
        return Arrays.<UniqueKey<UsersRecord>>asList(Keys.USERS_PKEY);
    }

    @Override
    public Users as(String alias) {
        return new Users(DSL.name(alias), this);
    }

    @Override
    public Users as(Name alias) {
        return new Users(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Users rename(String name) {
        return new Users(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Users rename(Name name) {
        return new Users(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, String, String, String, String, Long, Boolean, Boolean> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
