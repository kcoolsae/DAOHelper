/*
 * UpsertSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.util.ArrayList;

/**
 * Represents an upsert statement with initialized 'key'-values. Allows value-clauses. Finalize by calling {@link #execute}.
 */
public class UpsertSQLStatement extends InsertOrUpsert<UpsertSQLStatement> {

    private int numberOfKeys;

    UpsertSQLStatement(UpsertHeader header) {
        super(header.table, header.context);
        this.values= new ArrayList<>(header.values);
        this.numberOfKeys = header.values.size();
    }

    private UpsertSQLStatement(String table, BaseDAC context) {
        super(table, context);
    }

    public UpsertSQLStatement value(String name, Parameter value) {
        UpsertSQLStatement result = new UpsertSQLStatement(table, context);
        result.numberOfKeys = numberOfKeys;
        result.values = new ArrayList<>(this.values);
        result.values.add(new NamedParameter(name, value));
        return result;
    }

    public UpsertSQLStatement value(NamedParameterList parameters) {
        UpsertSQLStatement result = new UpsertSQLStatement(table, context);
        result.numberOfKeys = numberOfKeys;
        result.values = new ArrayList<>(this.values);
        parameters.addToParameterList(result.values);
        return result;
    }

    @Override
    protected String getFullStatement() {
        if (numberOfKeys == 0) {
            // behaves like an insert statement
            return super.getFullStatement();
        }

        StringBuilder builder = new StringBuilder(super.getFullStatement());
        builder.append (" ON CONFLICT (")
               .append (values.get(0).getName());
        for (int i=1; i < numberOfKeys; i++) {
            builder.append(",").append(values.get(i).getName());
        }
        builder.append(") DO UPDATE SET ")
                .append (values.get(numberOfKeys).getName())
                .append (" = EXCLUDED.")
                .append (values.get(numberOfKeys).getName());
        for (int i=numberOfKeys+1; i < values.size(); i++) {
            builder.append(",")
                    .append(values.get(i).getName())
                .append (" = EXCLUDED.")
                .append (values.get(i).getName());
        }
        return builder.toString();
    }
}
