/*
 * LocalTimeParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalTime;

/**
 * Implementation of {@link Parameter} for local date times.
 */
class LocalTimeParameter implements Parameter {

    private final LocalTime par;

    public LocalTimeParameter(LocalTime par) {
        this.par = par;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException{
        if (par == null) {
            ps.setNull(index, Types.TIME);
        } else {
            ps.setTime(index, Time.valueOf(par));
        }
    }
}
