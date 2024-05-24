/*
 * NotNullViolation.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

/**
 * Indicates a violation of a not null constraint.
 */
public class NotNullViolation extends DataAccessException {

    public NotNullViolation(Exception exception) {
        super("Not null violation", exception);
    }
}
