/*
 * NotFound.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

/**
 * This exception is thrown when a database search for a specific key or id
 * returns no result although a result was expected.
 */
public class NotFound extends DataAccessException {

    public NotFound(Exception ex) {
        super("Key not found", ex);
    }

    public NotFound(String message) { super(message); }

}
