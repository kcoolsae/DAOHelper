/*
 * ReopenTransactionTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DataAccessContext;
import be.ugent.caagt.dao.Provider;
import be.ugent.caagt.dao.UniqueViolation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks that it is possible to do more than a single transaction on the same connection.
 */
public class ReopenTransactionTest extends BaseDAO {

    // note: does not extend TestDao because we do not want to start and stop transaction automatically

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
    }

    @Test
    public void twoInserts () {
        DataAccessContext context = provider.getContext();
        setContext(context);

        context.begin();
        insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "John")
                .execute();
        assertThat(
                select("name").from("persons where first_name = 'John'")
                        .getString()
        ).isEqualTo("Doe");
        context.rollback();

        // should start second transaction
        context.begin();
        insertInto("persons")
                .value("name", "Doe2")
                .value("first_name", "John2")
                .execute();
        assertThat(
                select("name").from("persons where first_name = 'John2'")
                        .getString()
        ).isEqualTo("Doe2");
        context.rollback();

    }

    @Test
    public void firstInError () {
        DataAccessContext context = provider.getContext();
        setContext(context);

        try {
            context.begin();
            insertInto("stuff")
                    .value("id", 1)
                    .execute();
            // second insert should generate error
            insertInto("stuff")
                    .value("id", 1)
                    .execute();
        } catch (UniqueViolation ex) {
            context.rollback();
            // restarting transaction after the error
            context.begin();
        }
        insertInto("persons")
                .value("name", "Doe2")
                .value("first_name", "John2")
                .execute();
        assertThat(
                select("name").from("persons where first_name = 'John2'")
                        .getString()
        ).isEqualTo("Doe2");
        context.rollback();

    }

}
