/*
 * Intersection.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.util.List;

/**
 * Represents an intersection of several SELECT statements
 */
public class Intersection extends AbstractOrderedStatement {

    protected boolean isCompoundQuery() {
        return true;
    }

    Intersection(List<SelectSQLStatement> list, BaseDAC context) {
        super (createStatement(list), context);
        for (SelectSQLStatement selectSQLStatement : list) {
            params.addAll(selectSQLStatement.params);
        }
    }

    private static String createStatement (List<SelectSQLStatement> list) {
        int size = list.size();
        if (size == 0) {
            throw new IllegalArgumentException("There must be at least one query to intersect");
        } else if (size == 1) {
            return list.get(0).stat;
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("(").append(list.get(0).stat);
            for (int i = 1; i < size; i++) {
                builder.append(" INTERSECT ").append(list.get(i).stat);
            }
            return builder.append(")").toString();
        }
    }

    @Override
    public OrderedSQLStatement orderBy(OrderByClause clause) {
        return new OrderedSQLStatement(this, clause);
    }
}
