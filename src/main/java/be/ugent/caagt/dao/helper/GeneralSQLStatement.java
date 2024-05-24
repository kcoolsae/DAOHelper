/*
 * GeneralSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Represents a general sql statement
 */
public class GeneralSQLStatement extends AbstractSQLStatement implements SupportsParameter<GeneralSQLStatement> {

    private GeneralSQLStatement() {

    }

    GeneralSQLStatement(String stat, BaseDAC context) {
        super(stat, context);
    }

    /**
     * Create an object of this type containing the same information plus one additional parameter.
     */
    public GeneralSQLStatement parameter (Parameter par) {
        GeneralSQLStatement result = new GeneralSQLStatement();
        result.copyAux(this);
        result.addAux(par);
        return result;
    }

    public void execute() {
        executeUpdate();
    }


    @Override
    public <E extends Enum<E>> GeneralSQLStatement parameter(E value) {
        return parameter(new EnumParameter<>(value, context.getEnumClassTranslator()));
    }
}
