/*
 * AbstractSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract super class for all types of sql statements
 */
abstract class AbstractSQLStatement {

    protected String stat;

    protected List<Parameter> params;

    protected BaseDAC context;

    /**
     * Create a new object of this type.
     */
    protected AbstractSQLStatement(String stat, BaseDAC context) {
        this.stat = stat;
        this.params = new ArrayList<>();
        this.context = context;
    }

    /**
     * Creates an uninitialized object of this type. Should be initialized with {@link #copyAux}.
     */
    protected AbstractSQLStatement() {
        //
    }

    /**
     * Copies the state of the given statement.
     */
    protected void copyAux (AbstractSQLStatement src) {
        this.context = src.context;
        this.stat = src.stat;
        this.params = new ArrayList<>(src.params); // copy constructor of ArrayList gives 'unchecked assignment' warning
    }

    protected void addAux (Parameter parameter) {
        this.params.add(parameter);
    }

    //
    /**
     * Fills in all parameters. Too many parameters cause an {@link IllegalArgumentException}. Too few
     * parameters are only detected by the driver when the statement is executed.
     */
    protected void initParameters(PreparedStatement ps) throws SQLException {
        int index = 1;
        for (Parameter param : params) {
            try {
                param.setParameter(ps, index);
            } catch (SQLException ex) {
                if ("22023".equals(ex.getSQLState())) {
                    // invalid parameter value, e.g., the index is out of range
                    throw new IllegalArgumentException("Incorrect parameter " + index + ": " + ex.getMessage(), ex);
                } else {
                    throw ex;
                }
            }
            index++;
        }
    }

    protected PreparedStatement prepareStatement() throws SQLException {
        return context.getConnection().prepareStatement(stat);
    }

    /**
     * Execute an update statement after filling in all parameters
     */
    protected void executeUpdate() throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    private static long id = 0;

    protected static String getUniqueId () {
        id ++; // should never overflow
        return "__daohelper__" + id;
    }

}
