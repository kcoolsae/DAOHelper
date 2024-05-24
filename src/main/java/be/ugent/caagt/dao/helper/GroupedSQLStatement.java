/*
 * GroupedSQLStatement.java
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
 * Select statement with a group by clause
 */
public class GroupedSQLStatement extends AbstractOrderedStatement {

    private int numberOfHaving;

    GroupedSQLStatement(SelectSQLStatement src, String fields) {
        super();
        copyAux(src);
        this.stat += " GROUP BY " + fields;
        this.numberOfHaving = 0;
    }

    private GroupedSQLStatement() {

    }

    @Override
    public OrderedSQLStatement orderBy(OrderByClause clause) {
        return new OrderedSQLStatement(this, clause);
    }

    void addAuxHaving(WhereClause clause) {
        if (clause.hasParameter()) {
            addAux(clause.getParameter());
        }
        if (numberOfHaving == 0) {
            stat += " HAVING ";
        } else {
            stat += " AND ";
        }
        stat += clause.getClause();
        numberOfHaving++;
    }

    public GroupedSQLStatement having(StatementModifier modifier) {
        GroupedSQLStatement result = new GroupedSQLStatement();
        result.copyAux(this);
        result.numberOfHaving = this.numberOfHaving;
        modifier.addToHavingList(result);
        return result;
    }

    public GroupedSQLStatement having(String clause) {
        return having(new WhereClause(clause));
    }

    public GroupedSQLStatement having(String clause, boolean parameter) {
        return having(new WhereClause(clause, new BooleanParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, int parameter) {
        return having(new WhereClause(clause, new IntParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, double parameter) {
        return having(new WhereClause(clause, new DoubleParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, String parameter) {
        return having(new WhereClause(clause, new StringParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, LocalDateTime parameter) {
        return having(new WhereClause(clause, new LocalDateTimeParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, LocalDate parameter) {
        return having(new WhereClause(clause, new LocalDateParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, LocalTime parameter) {
        return having(new WhereClause(clause, new LocalTimeParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, Instant parameter) {
        return having(new WhereClause(clause, new InstantParameter(parameter)));
    }

    public GroupedSQLStatement having(String clause, byte[] parameter) {
        return having(new WhereClause(clause, new BytesParameter(parameter)));
    }

}
