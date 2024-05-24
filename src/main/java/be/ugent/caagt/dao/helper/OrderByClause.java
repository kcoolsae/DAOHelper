/*
 * OrderByClause.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Represents an 'order by' clause
 */
public class OrderByClause {

    private final String field;

    private final boolean ascending;

    public String getField() {
        return field;
    }

    public boolean isAscending() {
        return ascending;
    }

    public OrderByClause(String field, boolean ascending) {

        this.field = field;
        this.ascending = ascending;
    }
}
