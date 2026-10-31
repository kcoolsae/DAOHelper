/*
 * SQLProcedureCall.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DataAccessException;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Represents a call to a procedure
 */
public class SQLProcedureCall extends AbstractSQLStatement implements SupportsParameter<SQLProcedureCall> {

    private SQLProcedureCall() {

    }

    SQLProcedureCall(String call, BaseDAC context) {
        super(call, context);
    }

    /**
     * Create an object of this type containing the same information plus one additional parameter.
     */
    public SQLProcedureCall parameter(Parameter par) {
        SQLProcedureCall result = new SQLProcedureCall();
        result.copyAux(this);
        result.addAux(par);
        return result;
    }

    /**
     * Execute the call
     */
    public void execute() throws DataAccessException {
        try (CallableStatement ps = context.getConnection().prepareCall("{call " + stat + "}")) {
            initParameters(ps);
            ps.execute();
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

}
