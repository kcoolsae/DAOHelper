/*
 * LocalDateTimeParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

/**
 * Implementation of {@link Parameter} for local date times.
 */
class LocalDateTimeParameter implements Parameter {

    private final LocalDateTime par;

    public LocalDateTimeParameter(LocalDateTime par) {
        this.par = par;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException{
        if (par == null) {
            ps.setNull(index, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(index, Timestamp.valueOf(par));
        }
    }
}
