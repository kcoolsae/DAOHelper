/*
 * GettersTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.AnswerNotUnique;
import be.ugent.caagt.dao.NotFound;
import org.junit.Test;

import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the various getters of {@link AbstractQueryStatement}
 */
public class GettersTest extends FGTestBase {

    @Test
    public void getBoolean() {
        assertThat(
                select("flag").from("persons").where("name", "Bond").getBoolean()
        ).isTrue();
    }

    @Test
    public void getInt() {
        assertThat(
                select("number").from("persons").where("name", "Doe").getInt()
        ).isEqualTo(6);
    }
    @Test
    public void getBooleanNoResult() {
        assertThat(
                select("flag").from("persons").where("name", "Thompson").getBoolean()
        ).isFalse();
    }
    @Test
    public void getIntNoResult() {
        assertThat(
                select("number").from("persons").where("name", "Thompson").getInt()
        ).isZero();
    }

    @Test(expected = AnswerNotUnique.class)
    public void getBooleanTooManyResults() {
        select("flag").from("persons").getBoolean();
    }

    @Test(expected = NotFound.class)
    public void getOneBooleanNoResult() {
        select("flag").from("persons").where("name", "Thompson").getOneBoolean();
    }

    @Test(expected = NotFound.class)
    public void getOneIntNoResult() {
        select("number").from("persons").where("name", "Thompson").getOneInt();
    }

    @Test
    public void getStringNoResult() {
        assertThat(
                select("name").from("persons").where("name", "Thompson").getString()
        ).isNull();
    }

    @Test
    public void getStringOrDefault() {
        assertThat(
                select("name").from("persons").where("name", "Thompson").getStringOrDefault("SomeString")
        ).isEqualTo("SomeString");
    }

    @Test
    public void getStringOrEmpty() {
        assertThat(
                select("name").from("persons").where("name", "Thompson").getStringOrEmpty()
        ).isEmpty();
    }

    @Test(expected = NotFound.class)
    public void getOneStringNoResult() {
        select("name").from("persons").where("name", "Thompson").getOneString();
    }

    @Test
    public void getOneBoolean() {
        assertThat(
                select("flag").from("persons").where("name", "Bond").getOneBoolean()
        ).isTrue();
    }

    @Test
    public void getOneInt() {
        assertThat(
                select("number").from("persons").where("name", "Bond").getOneInt()
        ).isEqualTo(7);
    }
    @Test
    public void getOneString() {
        assertThat(
                select("first_name").from("persons").where("name", "Bond").getOneString()
        ).isEqualTo("James");
    }

    @Test
    public void notEmpty() {
        assertThat(
                select("1").from("persons").where("name LIKE '%o%'").isEmpty()
        ).isFalse();
    }

    @Test
    public void isEmpty() {
        assertThat(
                select("1").from("persons").where("number > ?", 70).isEmpty()
        ).isTrue();
    }

    @Test
    public void getLocalDateTime() {
        assertThat(
                select("registered").from("persons").where("name", "Bond").getLocalDateTime()
        ).isEqualTo(REGISTRATION_TIME);
        assertThat(
                select("registered").from("persons").where("name", "Doe").getOneLocalDateTime()
        ).isEqualTo(REGISTRATION_TIME);
    }

    @Test
    public void getLocalDate() {
        assertThat(
                select("birthday").from("persons").where("name", "Bond").getLocalDate()
        ).isEqualTo(BIRTHDAY);
        assertThat(
                select("birthday").from("persons").where("name", "Doe").getOneLocalDate()
        ).isEqualTo(BIRTHDAY);
    }

    @Test
    public void getLocalTime() {
        assertThat(
                select("starts_work_at").from("persons").where("name", "Bond").getLocalTime()
        ).isEqualTo(STARTS_WORK_AT);
        assertThat(
                select("starts_work_at").from("persons").where("name", "Doe").getOneLocalTime()
        ).isEqualTo(STARTS_WORK_AT);
    }

    @Test
    public void getInstant() {
        assertThat(
                select("stamp").from("persons").where("name", "Bond").getInstant()
        ).isEqualTo(STAMP);
        assertThat(
                select("stamp").from("persons").where("name", "Doe").getOneInstant()
        ).isEqualTo(STAMP);
    }

    @Test
    public void timeConvertersWithNames() {
        assertThat(
                select("birthday").from("persons").where("name", "Bond")
                .getOneObject((ResultSet rs) -> BaseDAO.getLocalDate(rs,"birthday"))
        ).isEqualTo(BIRTHDAY);
        assertThat(
                select("starts_work_at").from("persons").where("name", "Bond")
                .getOneObject((ResultSet rs) -> BaseDAO.getLocalTime(rs,"starts_work_at"))

        ).isEqualTo(STARTS_WORK_AT);
        assertThat(
                select("registered").from("persons").where("name", "Doe")
                        .getOneObject((ResultSet rs) -> BaseDAO.getLocalDateTime(rs,"registered"))
        ).isEqualTo(REGISTRATION_TIME);
        assertThat(
                select("stamp").from("persons").where("name", "Bond")
                        .getOneObject((ResultSet rs) -> BaseDAO.getInstant(rs,"stamp"))
        ).isEqualTo(STAMP);
    }
}
