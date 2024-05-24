/*
 * UpdateOrDeleteSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Represents an update or a delete statement
 */
public class UpdateOrDeleteSQLStatement extends AbstractFilteredStatement implements SupportsWhere<UpdateOrDeleteSQLStatement> {

    UpdateOrDeleteSQLStatement(UpdateSQLStatement src) {
        copyAux(src);
    }

    UpdateOrDeleteSQLStatement(String statement, BaseDAC context) {
        super(statement, context);
    }

    private UpdateOrDeleteSQLStatement() {
        super();
    }

    @Override
    public UpdateOrDeleteSQLStatement where(WhereClause clause) {
        UpdateOrDeleteSQLStatement result = new UpdateOrDeleteSQLStatement();
        result.copyAux(this);
        result.addAux(clause);
        return result;
    }

    public UpdateOrDeleteSQLStatement where(StatementModifier modifier) {
        UpdateOrDeleteSQLStatement result = new UpdateOrDeleteSQLStatement();
        result.copyAux(this);
        modifier.addToWhereList(result);
        return result;
    }

    @Override
    public <E extends Enum<E>> UpdateOrDeleteSQLStatement where(String clause, E parameter) {
        return where (new WhereClause(clause, parameter, context.getEnumClassTranslator()));
    }

    public void execute() {
        executeUpdate();
    }

}
