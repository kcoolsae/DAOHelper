/*
 * CallTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
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

    @Test
    public void untypedParameter() {
        // the type of the parameter is not inferred from the context
        assertThat(
                select("?").parameter(17).noFrom().getInt()
        ).isEqualTo(17);
    }

    @Test
    public void existsWithoutFrom() {
        insertInto("persons").value("name", "Doe").value("flag", false).execute();
        assertThat(
                select("EXISTS (SELECT 1 FROM persons WHERE name = ? AND NOT flag)")
                        .parameter("Doe").noFrom().getOneBoolean()
        ).isTrue();
    }

    @Test
    public void setReturningFunctionWithoutFrom() {
        assertThat(
                select("generate_series(1, ?)").parameter(3).noFrom().getList(rs -> rs.getInt(1))
        ).containsExactly(1, 2, 3);
    }
}
