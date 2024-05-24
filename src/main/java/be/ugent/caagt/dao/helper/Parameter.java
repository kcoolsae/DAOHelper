/*
 * Parameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Wraps a single parameter for a prepared statement. There are various implementations
 * of this interface for each type of the parameter.
 */
public interface Parameter {
    void setParameter (PreparedStatement ps, int index) throws SQLException;

    default String getQuestionMark() {
        return "?";
    }
}
