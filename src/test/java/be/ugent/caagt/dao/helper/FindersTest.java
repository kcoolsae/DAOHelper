/*
 * FindersTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
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

}
