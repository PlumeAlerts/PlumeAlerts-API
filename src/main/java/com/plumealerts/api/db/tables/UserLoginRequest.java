/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables;


import com.plumealerts.api.db.Indexes;
import com.plumealerts.api.db.Keys;
import com.plumealerts.api.db.Public;
import com.plumealerts.api.db.tables.records.UserLoginRequestRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserLoginRequest extends TableImpl<UserLoginRequestRecord> {

    private static final long serialVersionUID = 1414950405;

    /**
     * The reference instance of <code>public.user_login_request</code>
     */
    public static final UserLoginRequest USER_LOGIN_REQUEST = new UserLoginRequest();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserLoginRequestRecord> getRecordType() {
        return UserLoginRequestRecord.class;
    }

    /**
     * The column <code>public.user_login_request.state</code>.
     */
    public final TableField<UserLoginRequestRecord, String> STATE = createField("state", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.user_login_request.created_at</code>.
     */
    public final TableField<UserLoginRequestRecord, Timestamp> CREATED_AT = createField("created_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * The column <code>public.user_login_request.scopes</code>.
     */
    public final TableField<UserLoginRequestRecord, String> SCOPES = createField("scopes", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * Create a <code>public.user_login_request</code> table reference
     */
    public UserLoginRequest() {
        this(DSL.name("user_login_request"), null);
    }

    /**
     * Create an aliased <code>public.user_login_request</code> table reference
     */
    public UserLoginRequest(String alias) {
        this(DSL.name(alias), USER_LOGIN_REQUEST);
    }

    /**
     * Create an aliased <code>public.user_login_request</code> table reference
     */
    public UserLoginRequest(Name alias) {
        this(alias, USER_LOGIN_REQUEST);
    }

    private UserLoginRequest(Name alias, Table<UserLoginRequestRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserLoginRequest(Name alias, Table<UserLoginRequestRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> UserLoginRequest(Table<O> child, ForeignKey<O, UserLoginRequestRecord> key) {
        super(child, key, USER_LOGIN_REQUEST);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.USER_LOGIN_REQUEST_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UserLoginRequestRecord> getPrimaryKey() {
        return Keys.USER_LOGIN_REQUEST_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UserLoginRequestRecord>> getKeys() {
        return Arrays.<UniqueKey<UserLoginRequestRecord>>asList(Keys.USER_LOGIN_REQUEST_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLoginRequest as(String alias) {
        return new UserLoginRequest(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLoginRequest as(Name alias) {
        return new UserLoginRequest(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserLoginRequest rename(String name) {
        return new UserLoginRequest(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserLoginRequest rename(Name name) {
        return new UserLoginRequest(name, null);
    }
}
