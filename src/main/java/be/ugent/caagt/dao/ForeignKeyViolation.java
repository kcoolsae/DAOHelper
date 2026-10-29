/*
 * ForeignKeyViolation.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

/**
 * Indicates a violation of a foreign key when doing a database update or delete.
 */
public class ForeignKeyViolation extends DataAccessException {

    public ForeignKeyViolation(Exception exception) {
        super("Foreign key violation", exception);
    }
}
