/*
 * AnswerNotUnique.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

/**
 * This exception is thrown when more than one result is returned by a database query when only (at most) one was expected
 */
public class AnswerNotUnique extends DataAccessException {

    public AnswerNotUnique(Exception ex) {
        super("Answer not unique", ex);
    }

    public AnswerNotUnique(String msg) {
        super(msg);
    }

}
