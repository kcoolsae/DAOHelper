/*
 * DataAccessContext.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

import be.ugent.caagt.dao.helper.BaseDAC;

import java.sql.Connection;

/**
 * Data access context for testing
 */
public class DataAccessContext extends BaseDAC{

    DataAccessContext(Connection connection) {
        super(connection);
    }

}
