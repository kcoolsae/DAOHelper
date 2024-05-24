/*
 * SqlArrayParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class SqlArrayParameter implements Parameter {

    private final Object[] par;

    private final String typeName;

    public SqlArrayParameter(Object[] par, String typeName) {
        this.par = par;
        this.typeName = typeName;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        if (par != null) {
            Array array = ps.getConnection().createArrayOf(typeName, par);
            ps.setArray(index, array);
        } else {
            ps.setArray(index, null);
        }
    }
}
