/*
 * CallTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests calling stored procedures and functions
 */
public class CallTest extends TestDAO {

    @Test
    public void callWithParameters() {

        call("insert_person(?,?)")
                .parameter("voldemort")
                .parameter("lord")
                .execute();

        assertThat(
                select("name").from("persons").where("name", "voldemort").isEmpty()
        ).isFalse();

    }

    @Test
    public void functionCall() {
        assertThat(
                select("GREATEST(2,?)").parameter(15).noFrom().getInt()
        ).isEqualTo(15);
    }
}
