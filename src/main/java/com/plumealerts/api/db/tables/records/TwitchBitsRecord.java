/*
 * This file is generated by jOOQ.
 */
package com.plumealerts.api.db.tables.records;


import com.plumealerts.api.db.tables.TwitchBits;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
public class TwitchBitsRecord extends UpdatableRecordImpl<TwitchBitsRecord> implements Record7<Long, String, Boolean, String, String, Integer, Integer> {

    private static final long serialVersionUID = -2014288814;

    /**
     * Setter for <code>public.twitch_bits.notification_id</code>.
     */
    public void setNotificationId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.twitch_bits.notification_id</code>.
     */
    public Long getNotificationId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.twitch_bits.message_id</code>.
     */
    public void setMessageId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.twitch_bits.message_id</code>.
     */
    public String getMessageId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.twitch_bits.anonymous</code>.
     */
    public void setAnonymous(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.twitch_bits.anonymous</code>.
     */
    public Boolean getAnonymous() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>public.twitch_bits.message</code>.
     */
    public void setMessage(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.twitch_bits.message</code>.
     */
    public String getMessage() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.twitch_bits.message_username</code>.
     */
    public void setMessageUsername(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.twitch_bits.message_username</code>.
     */
    public String getMessageUsername() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.twitch_bits.bits_used</code>.
     */
    public void setBitsUsed(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.twitch_bits.bits_used</code>.
     */
    public Integer getBitsUsed() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>public.twitch_bits.total_bits</code>.
     */
    public void setTotalBits(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.twitch_bits.total_bits</code>.
     */
    public Integer getTotalBits() {
        return (Integer) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, String, Boolean, String, String, Integer, Integer> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, String, Boolean, String, String, Integer, Integer> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return TwitchBits.TWITCH_BITS.NOTIFICATION_ID;
    }

    @Override
    public Field<String> field2() {
        return TwitchBits.TWITCH_BITS.MESSAGE_ID;
    }

    @Override
    public Field<Boolean> field3() {
        return TwitchBits.TWITCH_BITS.ANONYMOUS;
    }

    @Override
    public Field<String> field4() {
        return TwitchBits.TWITCH_BITS.MESSAGE;
    }

    @Override
    public Field<String> field5() {
        return TwitchBits.TWITCH_BITS.MESSAGE_USERNAME;
    }

    @Override
    public Field<Integer> field6() {
        return TwitchBits.TWITCH_BITS.BITS_USED;
    }

    @Override
    public Field<Integer> field7() {
        return TwitchBits.TWITCH_BITS.TOTAL_BITS;
    }

    @Override
    public Long component1() {
        return getNotificationId();
    }

    @Override
    public String component2() {
        return getMessageId();
    }

    @Override
    public Boolean component3() {
        return getAnonymous();
    }

    @Override
    public String component4() {
        return getMessage();
    }

    @Override
    public String component5() {
        return getMessageUsername();
    }

    @Override
    public Integer component6() {
        return getBitsUsed();
    }

    @Override
    public Integer component7() {
        return getTotalBits();
    }

    @Override
    public Long value1() {
        return getNotificationId();
    }

    @Override
    public String value2() {
        return getMessageId();
    }

    @Override
    public Boolean value3() {
        return getAnonymous();
    }

    @Override
    public String value4() {
        return getMessage();
    }

    @Override
    public String value5() {
        return getMessageUsername();
    }

    @Override
    public Integer value6() {
        return getBitsUsed();
    }

    @Override
    public Integer value7() {
        return getTotalBits();
    }

    @Override
    public TwitchBitsRecord value1(Long value) {
        setNotificationId(value);
        return this;
    }

    @Override
    public TwitchBitsRecord value2(String value) {
        setMessageId(value);
        return this;
    }

    @Override
    public TwitchBitsRecord value3(Boolean value) {
        setAnonymous(value);
        return this;
    }

    @Override
    public TwitchBitsRecord value4(String value) {
        setMessage(value);
        return this;
    }

    @Override
    public TwitchBitsRecord value5(String value) {
        setMessageUsername(value);
        return this;
    }

    @Override
    public TwitchBitsRecord value6(Integer value) {
        setBitsUsed(value);
        return this;
    }

    @Override
    public TwitchBitsRecord value7(Integer value) {
        setTotalBits(value);
        return this;
    }

    @Override
    public TwitchBitsRecord values(Long value1, String value2, Boolean value3, String value4, String value5, Integer value6, Integer value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TwitchBitsRecord
     */
    public TwitchBitsRecord() {
        super(TwitchBits.TWITCH_BITS);
    }

    /**
     * Create a detached, initialised TwitchBitsRecord
     */
    public TwitchBitsRecord(Long notificationId, String messageId, Boolean anonymous, String message, String messageUsername, Integer bitsUsed, Integer totalBits) {
        super(TwitchBits.TWITCH_BITS);

        set(0, notificationId);
        set(1, messageId);
        set(2, anonymous);
        set(3, message);
        set(4, messageUsername);
        set(5, bitsUsed);
        set(6, totalBits);
    }
}
