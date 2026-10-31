/*
 * CompositeWhereClause.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.util.ArrayList;
import java.util.List;

public class CompositeWhereClause extends StatementModifier implements SupportsWhere<CompositeWhereClause> {

    private List<WhereClause> clauses;

    public CompositeWhereClause() {
        this.clauses = new ArrayList<>();
    }

    /**
     * @deprecated The translator is no longer used. Use {@link #CompositeWhereClause()} instead
     */
    @Deprecated
    public CompositeWhereClause(EnumClassNameTranslator enumClassNameTranslator) {
        this();
    }

    @Override
    public CompositeWhereClause where(WhereClause clause) {
        CompositeWhereClause result = new CompositeWhereClause();
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
}
