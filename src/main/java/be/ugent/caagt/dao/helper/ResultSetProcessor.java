/*
 * ResultSetProcessor.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Uses one row of a result set to change the state of a given object of some type
 */
@FunctionalInterface
public interface ResultSetProcessor<T> {
    void process(T object, ResultSet rs) throws SQLException;
}
