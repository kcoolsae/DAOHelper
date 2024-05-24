/*
 * OrderedSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Represents an SQL select statement with at least one ordered by clause. Allows orderBy but not where
 */
public class OrderedSQLStatement extends AbstractOrderedStatement {

    OrderedSQLStatement (AbstractSQLStatement src, OrderByClause clause) {
        copyAux(src);
        stat += " ORDER BY " + clause.getField() + (clause.isAscending() ? " ASC" : " DESC");
    }

    private OrderedSQLStatement() {

    }

    @Override
    public OrderedSQLStatement orderBy (OrderByClause clause) {
        OrderedSQLStatement result = new OrderedSQLStatement();
        result.copyAux(this);
        result.stat = stat + ", " + clause.getField() + (clause.isAscending() ? " ASC" : " DESC");
        return result;
    }

    public LimitedSQLStatement onlyPage(int pageNr, int pageSize) {
        return new LimitedSQLStatement(this, pageSize, pageNr*pageSize);
    }

}
