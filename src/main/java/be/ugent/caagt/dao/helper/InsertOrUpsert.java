/*
 * InsertOrUpsert.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Common super class for inserts and upserts
 */
abstract class InsertOrUpsert<T extends InsertOrUpsert<T>> {
    protected final String table;
    protected final BaseDAC context;

    protected List<NamedParameter> values;

    InsertOrUpsert(String table, BaseDAC context) {
        this.table = table;
        this.context = context;
    }

    public abstract T value(String name, Parameter parameter);

    public T value(String name, boolean value) {
        return value(name, new BooleanParameter(value));
    }

    public T value(String name, int value) {
        return value(name, new IntParameter(value));
    }

    public T value(String name, double value) {
        return value(name, new DoubleParameter(value));
    }

    public T value(String name, String value) {
        return value(name, new StringParameter(value));
    }

    public T value(String name, byte[] value) {
        return value(name, new BytesParameter(value));
    }

    public T value(String name, LocalDateTime value) {
        return value(name, new LocalDateTimeParameter(value));
    }

    public T value(String name, LocalDate value) {
        return value(name, new LocalDateParameter(value));
    }

    public T value(String name, LocalTime value) {
        return value(name, new LocalTimeParameter(value));
    }

    public T value(String name, Instant value) {
        return value(name, new InstantParameter(value));
    }

    public T value(String name, String[] value) {
        return value(name, value, "TEXT");
    }

    public T value(String name, String[][] value) {
        return value(name, value, "TEXT");
    }

    public T value(String name, Integer[] value) {
        return value(name, value, "INTEGER");
    }

    public T value(String name, Integer[][] value) {
        return value(name, value, "INTEGER");
    }

    public T value(String name, Double[] value) {
        return value(name, value, "float8");
    }

    public T value(String name, Double[][] value) {
        return value(name, value, "float8");
    }

    public T value(String name, int[] value) {
        return value(name, new IntArrayParameter(value));
    }

    public T value(String name, int[][] value) {
        return value(name, new Int2DimArrayParameter(value));
    }

    public T value(String name, double[] value) {
        return value(name, new DoubleArrayParameter(value));
    }

    public T value(String name, double[][] value) {
        return value(name, new Double2DimArrayParameter(value));
    }

    public T value (String name, Object[] value, String typeName) {
        return value(name, new SqlArrayParameter(value, typeName));
    }

    public <E extends Enum<E>> T value (String name, E value) {
        return value (name, new EnumParameter<>(value, context.getEnumClassTranslator()));
    }

    protected String getFullStatement() {
        if (values.isEmpty()) {
            // TODO: make this syntactically impossible
            throw new IllegalArgumentException("insert/upsert expects at least one value() or key()");
        }
        StringBuilder builder = new StringBuilder("INSERT INTO ");
        builder.append(table).append(" (");
        for (NamedParameter value : values) {
            builder.append(value.getName()).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length())
                .append(") VALUES (");
        for (NamedParameter value : values) {
            builder.append(value.getQuestionMark()).append(",");
        }
        builder.deleteCharAt(builder.length() - 1)
                .append(")");
        return builder.toString();
    }

    protected void initParameters(PreparedStatement ps) throws SQLException {
        int index = 1;
        for (NamedParameter value : values) {
            value.getParameter().setParameter(ps, index);
            index++;
        }
    }

    /**
     * Execute the insert or upsert statement
     */
    public void execute() throws DataAccessException {
        try (PreparedStatement ps = context.getConnection().prepareStatement(getFullStatement())) {
            initParameters(ps);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

}
