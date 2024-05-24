/*
 * OrderedTest.java
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
 * Tests ordered by clauses
 */
public class OrderedTest extends TestDAO {

    private int[] keys;

    @Before
    public void setup() {
        keys = new int[]{
            insertInto("persons")
                .value("name", "Bond")
                .value("first_name", "Jane")
                .create(),
            insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "Bernard")
                .create(),
            insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "Jane")
                .create(),
            insertInto("persons")
                .value("name", "Bond")
                .value("first_name", "James")
                .create()
        };
    }

    @Test
    public void doubleOrderBy() {
        assertThat(
                select ("id").from("persons")
                .orderBy(new OrderByClause("name", false))
                .orderBy("first_name")
        .getList (rs -> rs.getInt(1))
        ).containsExactly(keys[1], keys[2], keys[3], keys[0]);
    }
}
