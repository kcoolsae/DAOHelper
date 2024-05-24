/*
 * UpdateSQLStatement.java
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

/**
 * Fist part of an update statement. Supports 'set' and 'where'
 */
public class UpdateSQLStatement extends AbstractSQLStatement implements SupportsWhere<UpdateOrDeleteSQLStatement> {

    private int numberOfSets;

    UpdateSQLStatement(String table, BaseDAC context) {
        super("UPDATE " + table + " SET ", context);
        this.numberOfSets = 0;
    }

    private UpdateSQLStatement() {

    }

    UpdateSQLStatement set(String name, Parameter parameter) {
        UpdateSQLStatement result = new UpdateSQLStatement();
        result.copyAux(this);
        result.numberOfSets = this.numberOfSets;
        result.addAuxSet(name, parameter);
        return result;
    }

    void addAuxSet(String name, Parameter parameter) {
        addAux(parameter);
        if (numberOfSets > 0) {
            stat += ", ";
        }
        name = name.indexOf('?') < 0 ? name + "=" + parameter.getQuestionMark() : name;
        stat += name;
        numberOfSets = numberOfSets + 1;
    }

    public UpdateSQLStatement set(NamedParameterList list) {
        UpdateSQLStatement result = new UpdateSQLStatement();
        result.copyAux(this);
        result.numberOfSets = this.numberOfSets;
        list.addToSetList(result);
        return result;
    }

    public UpdateSQLStatement set(String clause) {
        if (clause.indexOf('?') >= 0) {
            throw new IllegalArgumentException("Set without parameter must not contain a question mark");
        }
        UpdateSQLStatement result = new UpdateSQLStatement();
        result.copyAux(this);
        result.numberOfSets = numberOfSets + 1;
        if (numberOfSets > 0) {
            result.stat += ", ";
        }
        result.stat += clause;
        return result;
    }

    public UpdateSQLStatement set(String name, boolean parameter) {
        return set(name, new BooleanParameter(parameter));
    }

    public UpdateSQLStatement set(String name, int parameter) {
        return set(name, new IntParameter(parameter));
    }

    public UpdateSQLStatement set(String name, double parameter) {
        return set(name, new DoubleParameter(parameter));
    }

    public UpdateSQLStatement set(String name, String parameter) {
        return set(name, new StringParameter(parameter));
    }

    public UpdateSQLStatement set(String name, byte[] parameter) {
        return set(name, new BytesParameter(parameter));
    }

    public UpdateSQLStatement set(String name, LocalDateTime parameter) {
        return set(name, new LocalDateTimeParameter(parameter));
    }

    public UpdateSQLStatement set(String name, LocalDate parameter) {
        return set(name, new LocalDateParameter(parameter));
    }

    public UpdateSQLStatement set(String name, LocalTime parameter) {
        return set(name, new LocalTimeParameter(parameter));
    }

    public UpdateSQLStatement set(String name, Instant parameter) {
        return set(name, new InstantParameter(parameter));
    }

    public UpdateSQLStatement set(String name, Object[] parameter, String typeName) {
        return set(name, new SqlArrayParameter(parameter, typeName));
    }

    public UpdateSQLStatement set(String name, String[] parameter) {
        return set(name, parameter, "TEXT");
    }

    public UpdateSQLStatement set(String name, Integer[] parameter) {
        return set(name, parameter, "INTEGER");
    }

    public UpdateSQLStatement set(String name, int[] parameter) {
        return set(name, BoxUtils.boxArray(parameter), "INTEGER");
    }

    public UpdateSQLStatement set(String name, Integer[][] parameter) {
        return set(name, parameter, "INTEGER");
    }

    public UpdateSQLStatement set(String name, int[][] parameter) {
        return set(name, BoxUtils.boxArray(parameter), "INTEGER");
    }

    public UpdateSQLStatement set(String name, Double[] parameter) {
        return set(name, parameter, "float8");
    }

    public UpdateSQLStatement set(String name, double[] parameter) {
        return set(name, BoxUtils.boxArray(parameter), "float8");
    }

    public UpdateSQLStatement set(String name, Double[][] parameter) {
        return set(name, parameter, "float8");
    }

    public UpdateSQLStatement set(String name, double[][] parameter) {
        return set(name, BoxUtils.boxArray(parameter), "float8");
    }

    public <E extends Enum<E>> UpdateSQLStatement set(String name, E parameter) {
        return set(name, new EnumParameter<>(parameter, context.getEnumClassTranslator()));
    }

    @Override
    public UpdateOrDeleteSQLStatement where(WhereClause clause) {
        return new UpdateOrDeleteSQLStatement(this).where(clause);
    }

    public UpdateOrDeleteSQLStatement where(StatementModifier modifier) {
        return new UpdateOrDeleteSQLStatement(this).where(modifier);
    }

    @Override
    public <E extends Enum<E>> UpdateOrDeleteSQLStatement where(String clause, E parameter) {
        return where(new WhereClause(clause, parameter, context.getEnumClassTranslator()));
    }
}
