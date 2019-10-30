/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables;


import com.plumealerts.api.db.Indexes;
import com.plumealerts.api.db.Keys;
import com.plumealerts.api.db.Public;
import com.plumealerts.api.db.tables.records.ScopesRecord;
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
public class Scopes extends TableImpl<ScopesRecord> {

    private static final long serialVersionUID = 1491985708;

    /**
     * The reference instance of <code>public.scopes</code>
     */
    public static final Scopes SCOPES = new Scopes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScopesRecord> getRecordType() {
        return ScopesRecord.class;
    }

    /**
     * The column <code>public.scopes.scope</code>.
     */
    public final TableField<ScopesRecord, String> SCOPE = createField(DSL.name("scope"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * Create a <code>public.scopes</code> table reference
     */
    public Scopes() {
        this(DSL.name("scopes"), null);
    }

    /**
     * Create an aliased <code>public.scopes</code> table reference
     */
    public Scopes(String alias) {
        this(DSL.name(alias), SCOPES);
    }

    /**
     * Create an aliased <code>public.scopes</code> table reference
     */
    public Scopes(Name alias) {
        this(alias, SCOPES);
    }

    private Scopes(Name alias, Table<ScopesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Scopes(Name alias, Table<ScopesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Scopes(Table<O> child, ForeignKey<O, ScopesRecord> key) {
        super(child, key, SCOPES);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.SCOPES_PKEY);
    }

    @Override
    public UniqueKey<ScopesRecord> getPrimaryKey() {
        return Keys.SCOPES_PKEY;
    }

    @Override
    public List<UniqueKey<ScopesRecord>> getKeys() {
        return Arrays.<UniqueKey<ScopesRecord>>asList(Keys.SCOPES_PKEY);
    }

    @Override
    public Scopes as(String alias) {
        return new Scopes(DSL.name(alias), this);
    }

    @Override
    public Scopes as(Name alias) {
        return new Scopes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Scopes rename(String name) {
        return new Scopes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Scopes rename(Name name) {
        return new Scopes(name, null);
    }

    // -------------------------------------------------------------------------
    // Row1 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row1<String> fieldsRow() {
        return (Row1) super.fieldsRow();
    }
}
