/*
 * FindersTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.AnswerNotUnique;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the various find methods of {@link AbstractQueryStatement}
 */
public class FindersTest extends FGTestBase {

    @Test
    public void findBoolean() {
        assertThat(
                select("flag").from("persons").where("name", "Bond").findBoolean()
        ).isPresent().hasValue(true);
    }

    @Test
    public void findInt() {
        assertThat(
                select("number").from("persons").where("name", "Doe").findInt()
        ).isPresent().hasValue(6);
    }
    
    @Test
    public void findBooleanNoResult() {
        assertThat(
                select("flag").from("persons").where("name", "Thompson").findBoolean()
        ).isNotPresent();
    }
    
    @Test
    public void findIntNoResult() {
        assertThat(
                select("number").from("persons").where("name", "Thompson").findInt()
        ).isNotPresent();
    }

    @Test(expected = AnswerNotUnique.class)
    public void findBooleanTooManyResults() {
        select("flag").from("persons").findBoolean();
    }

    @Test
    public void findStringNoResult() {
        assertThat(
                select("name").from("persons").where("name", "Thompson").findString()
        ).isNotPresent();
    }


    @Test
    public void findLocalDateTime() {
        assertThat(
                select("registered").from("persons").where("name", "Bond").findLocalDateTime()
        ).isPresent().hasValue(REGISTRATION_TIME);
    }

    @Test
    public void findLocalDate() {
        assertThat(
                select("birthday").from("persons").where("name", "Bond").findLocalDate()
        ).isPresent().hasValue(BIRTHDAY);
    }

    @Test
    public void findLocalTime() {
        assertThat(
                select("starts_work_at").from("persons").where("name", "Bond").findLocalTime()
        ).isPresent().hasValue(STARTS_WORK_AT);
    }

    @Test
    public void findInstant() {
        assertThat(
                select("stamp").from("persons").where("name", "Bond").findInstant()
        ).isPresent().hasValue(STAMP);
    }

    private void insertNobody() {
        insertInto("persons").value("name", "Nobody").execute();
    }

    @Test
    public void findNullValues() {
        insertNobody();
        assertThat(
                select("flag").from("persons").where("name", "Nobody").findBoolean()
        ).isNotPresent();
        assertThat(
                select("number").from("persons").where("name", "Nobody").findInt()
        ).isNotPresent();
        assertThat(
                select("number::float8").from("persons").where("name", "Nobody").findDouble()
        ).isNotPresent();
        assertThat(
                select("first_name").from("persons").where("name", "Nobody").findString()
        ).isNotPresent();
        assertThat(
                select("registered").from("persons").where("name", "Nobody").findLocalDateTime()
        ).isNotPresent();
        assertThat(
                select("birthday").from("persons").where("name", "Nobody").findLocalDate()
        ).isNotPresent();
        assertThat(
                select("starts_work_at").from("persons").where("name", "Nobody").findLocalTime()
        ).isNotPresent();
        assertThat(
                select("stamp").from("persons").where("name", "Nobody").findInstant()
        ).isNotPresent();
    }

    @Test
    public void findZeroAndFalse() {
        update("persons").set("number", 0).where("name", "Doe").execute();
        assertThat(
                select("number").from("persons").where("name", "Doe").findInt()
        ).isPresent().hasValue(0);
        assertThat(
                select("number::float8").from("persons").where("name", "Doe").findDouble()
        ).isPresent().hasValue(0.0);
        assertThat(
                select("flag").from("persons").where("name", "Doe").findBoolean()
        ).isPresent().hasValue(false);
    }

    @Test
    public void findObjectKeepsRowWithNullValue() {
        insertNobody();
        assertThat(
                select("name, first_name").from("persons").where("name", "Nobody")
                        .findObject(rs -> rs.getString("name") + "/" + rs.getString("first_name"))
        ).isPresent().hasValue("Nobody/null");
    }

}
