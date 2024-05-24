/*
 * UpsertTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DataAccessException;
import be.ugent.caagt.dao.UniqueViolation;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests upserts.
 */
public class UpsertTest extends TestDAO {

    @Before
    public void setup() {
        insertInto("stuff")
                .value("id", 123).value("val1", "otto").value("val2", 13)
                .execute();
        insertInto("things")
                .value("id1", 77).value("id2", "romeo")
                .value("val1", "julia").value("val2", 88)
                .execute();
    }

    @Test
    public void nonexisting() {
        // like insert
        insertOrUpdateInto("stuff")
                .value("id", 321).value("val1", "gerard").value("val2", 15)
                .execute();
        assertThat(
                select("val1").from("stuff").where("id", 321).getString()
        ).isEqualTo("gerard");

        // with key
        insertOrUpdateInto("stuff")
                .key("id", 321).value("val1", "gerard").value("val2", 15)
                .execute();
        assertThat(
                select("val2").from("stuff").where("id", 321).getInt()
        ).isEqualTo(15);
    }

    @Test
    public void existing() {
        // with key
        insertOrUpdateInto("stuff")
                .key("id", 123).value("val1", "gerard").value("val2", 15)
                .execute();
        assertUpsertGerard15();
    }

    private void assertUpsertGerard15() {
        assertThat(
                select("val2").from("stuff").where("id", 123).getInt()
        ).isEqualTo(15);
        assertThat(
                select("val1").from("stuff").where("id", 123).getString()
        ).isEqualTo("gerard");
    }

    private NamedParameterList val1val2(String val1, int val2) {
        return new NamedParameterList()
                .with("val1", val1)
                .with("val2", val2);
    }

    @Test
    public void upsertParameterList() {
        insertOrUpdateInto("stuff")
                .key("id", 123)
                .value(val1val2("gerard", 15))
                .execute();
        assertUpsertGerard15();
    }

    @Test
    public void upsertKeyParameterList() {
        insertOrUpdateInto("stuff")
                .key(new NamedParameterList().with("id", 123))
                .value("val1", "gerard").value("val2", 15)
                .execute();
        assertUpsertGerard15();
    }

    @Test(expected = DataAccessException.class)
    public void invalidNumberOfKeys() {
        insertOrUpdateInto("stuff")
                .key("id", 123).key("val1", "gerard").value("val2", 15)
                .execute();
    }

    @Test(expected = UniqueViolation.class)
    public void uniqueKeyViolation() {
        insertOrUpdateInto("stuff")
                .value("id", 123).value("val1", "gerard").value("val2", 15)
                .execute();
    }

    @Test
    public void doubleKeys() {
        insertOrUpdateInto("things")
                .key("id1", 77).key("id2", "romeo")
                .execute();
        assertThat(
                select("val1 || val2").from("things")
                        .where("id1", 77).where("id2", "romeo")
                        .getString()
        ).isEqualTo("julia88");

        insertOrUpdateInto("things")
                .key("id1", 77).key("id2", "romeo")
                .value("val1", "capulet")
                .execute();
        assertThat(
                select("val1 || val2").from("things")
                        .where("id1", 77).where("id2", "romeo")
                        .getString()
        ).isEqualTo("capulet88");

    }

}
