/*
 * SelectHeader.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Very first part of a select statement, Requires call to {@link #from}.
 */
public class SelectHeader extends AbstractSQLStatement implements SupportsParameter<SelectHeader> {

    SelectHeader(String fields, BaseDAC context) {
        super ("SELECT " + fields, context);
    }

    private SelectHeader() {

    }

    public SelectSQLStatement from(String clause) {
        return new SelectSQLStatement(this, clause);
    }

    /**
     * Perform a select without from-clause. Use this to call functions stored in the database, or
     * for other expressions that do not need a table, such as {@code nextval('mySequence')} or
     * {@code EXISTS (SELECT ...)}.
     */
    public RestrictedSelectSQLStatement noFrom() {
        return new RestrictedSelectSQLStatement(this);
    }

    @Override
    public SelectHeader parameter(Parameter p) {
        SelectHeader result = new SelectHeader();
        result.copyAux(this);
        result.addAux(p);
        return result;
    }

}
