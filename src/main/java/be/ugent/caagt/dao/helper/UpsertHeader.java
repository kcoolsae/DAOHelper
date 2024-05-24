/*
 * UpsertHeader.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Represents an upsert statement. Allows key clauses followed by value clauses. Can be finalizes by calling {@link #execute}
 * but then no updates will be done in case of conflict.
 */
public class UpsertHeader extends InsertOrUpsert<UpsertSQLStatement> {

    UpsertHeader(String table, BaseDAC context) {
        super(table, context);
        this.values = new ArrayList<>();
    }

    @Override
    public UpsertSQLStatement value(String name, Parameter parameter) {
        return new UpsertSQLStatement(this).value(name, parameter);
    }

    public UpsertSQLStatement value(NamedParameterList parameters) {
        return new UpsertSQLStatement(this).value(parameters);
    }

    private UpsertHeader copyAndAdd(String name, Parameter value) {
        UpsertHeader result = new UpsertHeader(table, context);
        result.values = new ArrayList<>(this.values);
        result.values.add(new NamedParameter(name, value));
        return result;
    }

    public UpsertHeader key(NamedParameterList list) {
        UpsertHeader result = new UpsertHeader(table, context);
        result.values = new ArrayList<>(this.values);
        list.addToParameterList(result.values);
        return result;
    }

    public UpsertHeader key(String name, boolean value) {
        return copyAndAdd(name, new BooleanParameter(value));
    }

    public UpsertHeader key(String name, int value) {
        return copyAndAdd(name, new IntParameter(value));
    }

    public UpsertHeader key(String name, double value) {
        return copyAndAdd(name, new DoubleParameter(value));
    }

    public UpsertHeader key(String name, String value) {
        return copyAndAdd(name, new StringParameter(value));
    }

    public UpsertHeader key(String name, LocalDateTime value) {
        return copyAndAdd(name, new LocalDateTimeParameter(value));
    }

    public UpsertHeader key(String name, LocalDate value) {
        return copyAndAdd(name, new LocalDateParameter(value));
    }

    public UpsertHeader key(String name, LocalTime value) {
        return copyAndAdd(name, new LocalTimeParameter(value));
    }

    public UpsertHeader key(String name, Instant value) {
        return copyAndAdd(name, new InstantParameter(value));
    }

    public UpsertHeader key(String name, byte[] value) {
        return copyAndAdd(name, new BytesParameter(value));
    }

    public <E extends Enum<E>> UpsertHeader key(String name, E value) {
        return copyAndAdd(name, new EnumParameter<>(value, context.getEnumClassTranslator()));
    }

    protected String getFullStatement() {
        return super.getFullStatement() + " ON CONFLICT DO NOTHING";
    }

}
