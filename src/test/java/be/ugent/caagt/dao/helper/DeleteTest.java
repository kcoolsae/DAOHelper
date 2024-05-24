/*
 * DeleteTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test delete operations
 */
public class DeleteTest extends TestDAO {

    private int key;

    @Before
    public void setup() {
        this.key = insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "John")
                .create();
    }

    private void checkJohnDeleted() {
        assertThat(
                select("count(*)").from("persons").getInt()
        ).isEqualTo(0);
    }

    @Test
    public void simpleDelete() {
        deleteFrom("persons").where("id", key).execute();
        checkJohnDeleted();
    }

    @Test
    public void multiWhere() {
        deleteFrom("persons")
                .where(compositeWhereClause().where("id", key))
                .execute();
        checkJohnDeleted();
    }
}
