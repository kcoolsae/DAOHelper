/*
 * WhereClause.java
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
 * Represents a where clause in an sql statement
 */
public class WhereClause extends StatementModifier {

    private final String clause;

    private final Parameter parameter;

    private final boolean hasParameter;

    WhereClause(String clause, Parameter parameter) {
        this.clause = clause.indexOf('?') < 0 ? clause + " = " + parameter.getQuestionMark() : clause;
        this.parameter = parameter;
        this.hasParameter = true;
    }

    WhereClause(NamedParameter namedParameter) {
        this (namedParameter.getName(), namedParameter.getParameter());
    }

    WhereClause(String clause) {
        this.clause = clause;
        if (clause.indexOf('?') >= 0) {
            throw new IllegalArgumentException("Where clause without parameter must not contain a question mark");
        }
        this.parameter = null;
        this.hasParameter = false;
    }

    public WhereClause(String clause, boolean parameter) {
        this (clause, new BooleanParameter(parameter));
    }

    public WhereClause(String clause, int parameter) {
        this (clause, new IntParameter(parameter));
    }

    public WhereClause(String clause, double parameter) {
        this (clause, new DoubleParameter(parameter));
    }

    public WhereClause(String clause, String parameter) {
        this (clause, new StringParameter(parameter));
    }

    public WhereClause(String clause, byte[] parameter) {
        this (clause, new BytesParameter(parameter));
    }

    public WhereClause(String clause, LocalDateTime parameter) {
        this (clause, new LocalDateTimeParameter(parameter));
    }

    public WhereClause(String clause, LocalDate parameter) {
        this (clause, new LocalDateParameter(parameter));
    }

    public WhereClause(String clause, LocalTime parameter) {
        this (clause, new LocalTimeParameter(parameter));
    }

    public WhereClause(String clause, Instant parameter) {
        this (clause, new InstantParameter(parameter));
    }

    public <E extends Enum<E>> WhereClause (String clause, E parameter, EnumClassNameTranslator translator) {
        this (clause, new EnumParameter<>(parameter, translator));
    }

    String getClause() {
        return clause;
    }

    boolean hasParameter() {
        return hasParameter;
    }

    Parameter getParameter() {
        return parameter;
    }

    protected void addToWhereList (AbstractFilteredStatement statement) {
        statement.addAux(this);
    }

    @Override
    protected void addToHavingList(GroupedSQLStatement statement) {
        statement.addAuxHaving(this);
    }
}
