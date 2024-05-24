/*
 * TestDAO.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.Provider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Common superclass of all dao tests
 */
public class TestDAO extends BaseDAO {

    private static Provider provider;

    @BeforeClass
    public static void createProvider() {
        provider = new Provider();
    }

    @AfterClass
    public static void releaseProvider() {
        provider = null;
    }

    @Before
    public void create() {
        setContext(provider.getContext());
        getContext().begin();
    }

    @After
    public void close() {
        getContext().rollback();
        getContext().close();
    }
}
