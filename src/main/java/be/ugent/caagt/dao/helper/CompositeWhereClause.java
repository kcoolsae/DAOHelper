/*
 * CompositeWhereClause.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.util.ArrayList;
import java.util.List;

public class CompositeWhereClause extends StatementModifier implements SupportsWhere<CompositeWhereClause> {

    private List<WhereClause> clauses;

    private final EnumClassNameTranslator enumClassNameTranslator;

    public CompositeWhereClause(EnumClassNameTranslator enumClassNameTranslator) {
        this.enumClassNameTranslator = enumClassNameTranslator;
        this.clauses = new ArrayList<>();
    }

    /**
     * @deprecated Use method {@code compositeWhereClause} from DAO or DAC instead
     */
    @Deprecated
    public CompositeWhereClause() {
        this (EnumClassNameTranslator.SNAKE_CASE);
    }

    @Override
    public CompositeWhereClause where(WhereClause clause) {
        CompositeWhereClause result = new CompositeWhereClause(enumClassNameTranslator);
        result.clauses = new ArrayList<>(clauses);
        result.clauses.add(clause);
        return result;
    }

    @Override
    protected void addToWhereList(AbstractFilteredStatement statement) {
        for (WhereClause clause : clauses) {
            statement.addAux(clause);
        }
    }

    @Override
    protected void addToHavingList(GroupedSQLStatement statement) {
        for (WhereClause clause : clauses) {
            statement.addAuxHaving(clause);
        }
    }

    @Override
    public <E extends Enum<E>> CompositeWhereClause where(String clause, E parameter) {
        return where(new WhereClause(clause, parameter, enumClassNameTranslator));
    }
}
