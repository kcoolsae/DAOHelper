/*
 * DoubleParameter.java
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
 * Implementation of {@link Parameter} for doubles.
 */
public class DoubleParameter implements Parameter {

    private final double par;

    public DoubleParameter(double par) {
        this.par = par;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException{
        ps.setDouble(index, par);
    }
}
