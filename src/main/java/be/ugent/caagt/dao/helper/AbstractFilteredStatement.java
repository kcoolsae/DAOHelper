/*
 * AbstractFilteredStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Common super class of statements that admit a chain of where clauses
 */
public abstract class AbstractFilteredStatement extends AbstractSQLStatement {

    protected int nrOfWhereClauses;

    protected AbstractFilteredStatement(String stat, BaseDAC context) {
        super(stat, context);
    }

    /**
     * Creates an uninitialized object of this type. Should be initialized with {@link #copyAux}.
     */
    protected AbstractFilteredStatement() {
        //
    }

    protected void copyAux(AbstractFilteredStatement src) {
        super.copyAux(src);
        this.nrOfWhereClauses = src.nrOfWhereClauses;
    }

    protected void addAux (WhereClause clause) {
        if (clause.hasParameter()) {
            addAux(clause.getParameter());
        }
        if (nrOfWhereClauses == 0) {
            stat += " WHERE ";
        } else {
            stat += " AND ";
        }
        stat += clause.getClause();
        nrOfWhereClauses++;
    }

}
