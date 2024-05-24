/*
 * BaseDAC.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DACException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Base class from which your data access context class should be derived.
 */
public abstract class BaseDAC implements AutoCloseable {

    private final Connection connection;

    /**
     * Create a data access context which uses the given JDBC connection.
     */
    protected BaseDAC (Connection connection) {
        this.connection = connection;
    }

    /**
     * Start a transaction on this context
     */
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new DACException("Auto commit could not be disabled", ex);
        }
    }

    /**
     * Commit the current transaction
     */
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new DACException("Commit error", ex);
        }
    }

    /**
     * Roll back the current transaction
     */
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new DACException("Rollback error", ex);
        }
    }

    /**
     * Close
     */
    public void close() {
        try {
            connection.setAutoCommit(true);
               // needed in case of connection pooling,
               // also commits current transaction as a bonus, but we do not want to rely on this
            connection.close();
        } catch(SQLException ex){
            throw new DACException("Close error", ex);
        }
    }

    /**
     * The current JDBC connection associated to this context
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Provides translation of java enum class names to database enum types.
     * Default implementation converts to snake case, but can be overridden by clients.
     */
    protected EnumClassNameTranslator getEnumClassTranslator () {
        return EnumClassNameTranslator.SNAKE_CASE;
    }

    /**
     * Create a composite where clause
     */
    protected CompositeWhereClause compositeWhereClause() {
        return new CompositeWhereClause(getEnumClassTranslator());
    }

}
