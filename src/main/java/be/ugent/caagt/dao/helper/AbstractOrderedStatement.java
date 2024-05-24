/*
 * AbstractOrderedStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Common super class of queries that are ordered or can be ordered
 */
abstract class AbstractOrderedStatement extends AbstractQueryStatement {

    /**
     * Creates an uninitialized object of this type. Should be initialized with {@link #copyAux}.
     */
    protected AbstractOrderedStatement() {
    }

    protected  AbstractOrderedStatement(String stat, BaseDAC context) {
        super(stat, context);
    }

    public abstract OrderedSQLStatement orderBy (OrderByClause clause);

    public OrderedSQLStatement orderBy (String field) {
        return orderBy(field, true);
    }

    public OrderedSQLStatement orderBy (String field, boolean ascending) {
        return orderBy(new OrderByClause(field, ascending));
    }
}
