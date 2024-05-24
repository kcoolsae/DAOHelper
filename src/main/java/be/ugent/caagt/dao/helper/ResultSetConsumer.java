/*
 * ResultSetConsumer.java
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
 * Processes one row in a result set
 */
@FunctionalInterface
public interface ResultSetConsumer {
    void accept (ResultSet rs) throws SQLException;
}
