/*
 * DACException.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

/**
 * Special @link{DataAccessException} that occurs at the data access context level
 */
public class DACException extends DataAccessException {

    public DACException (String message, Exception ex) {
        super (message, ex);
    }
}
