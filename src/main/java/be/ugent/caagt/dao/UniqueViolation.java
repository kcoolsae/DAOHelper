/*
 * UniqueViolation.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

/**
 * Indicates a violation of a uniqueness contstraint when doing a database update
 */
public class UniqueViolation extends DataAccessException {

    public UniqueViolation(Exception exception) {
        super("Unique violation", exception);
    }
}
