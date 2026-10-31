/*
 * UpdateFromSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Part of an update statement up to and including the from clause. Supports 'parameter' (for question marks
 * in the from clause) and 'where', but no further 'set'. Cannot be executed without where clause.
 */
public class UpdateFromSQLStatement extends AbstractSQLStatement
        implements SupportsParameter<UpdateFromSQLStatement>, SupportsWhere<UpdateOrDeleteSQLStatement> {

    UpdateFromSQLStatement(UpdateSQLStatement src, String clause) {
        copyAux(src);
        stat += " FROM " + clause;
    }

    private UpdateFromSQLStatement() {

    }

    /**
     * Create an object of this type containing the same information plus one additional parameter.
     */
    @Override
    public UpdateFromSQLStatement parameter(Parameter par) {
        UpdateFromSQLStatement result = new UpdateFromSQLStatement();
        result.copyAux(this);
        result.addAux(par);
        return result;
    }

    @Override
    public UpdateOrDeleteSQLStatement where(WhereClause clause) {
        return new UpdateOrDeleteSQLStatement(this).where(clause);
    }

    public UpdateOrDeleteSQLStatement where(StatementModifier modifier) {
        return new UpdateOrDeleteSQLStatement(this).where(modifier);
    }
}
