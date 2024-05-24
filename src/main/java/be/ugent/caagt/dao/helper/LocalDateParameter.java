/*
 * LocalDateParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.*;
import java.time.LocalDate;

/**
 * Implementation of {@link Parameter} for local date times.
 */
class LocalDateParameter implements Parameter {

    private final LocalDate par;

    public LocalDateParameter(LocalDate par) {
        this.par = par;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException{
        if (par == null) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, Date.valueOf(par));
        }
    }
}
