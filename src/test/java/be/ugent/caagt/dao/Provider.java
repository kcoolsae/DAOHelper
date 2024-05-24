/*
 * Provider.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides connections to an embedded Postgres database used for testing. See Readme.db for details.
 */
public class Provider {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost/daohelpertestdb?user=daohelpertestuser&password=daohelper"
        );
    }

    private void truncateTable() {
        try (Connection connection = getConnection();
             Statement stat = connection.createStatement()) {
            stat.executeUpdate("TRUNCATE TABLE persons");
        } catch (SQLException ex) {
            throw new RuntimeException("Could not truncate table", ex);
        }
    }

    public Provider() {
        truncateTable();
    }

    public DataAccessContext getContext() {
        try {
            return new DataAccessContext(getConnection());
        } catch (SQLException ex) {
            throw new DataAccessException("Could not obtain connection", ex);
        }
    }

}
