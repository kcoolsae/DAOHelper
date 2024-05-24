/*
 * RestrictedSelectSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Represents a select statement for the special case where no table is given.
 * This kind of statement is particularly useful when calling functions.
 * No with, ordered by or limit clauses are allowed here.
 */
public class RestrictedSelectSQLStatement extends AbstractQueryStatement {

    RestrictedSelectSQLStatement(SelectHeader src) {
        copyAux(src);
    }


}
