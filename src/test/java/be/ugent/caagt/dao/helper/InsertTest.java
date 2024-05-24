/*
 * InsertTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test insert operations
 */
public class InsertTest extends TestDAO {

    @Test
    public void insertWithParameters() {
        LocalDateTime dateTime = LocalDateTime.of(2014, 4, 1, 12, 0);
        Instant instant = Instant.ofEpochSecond(120000);
        insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "John")
                .value("number", 13)
                .value("registered", dateTime)
                .value("stamp", instant)
                .execute();
        assertInsertedAux(dateTime, instant);
    }

    @Test
    public void insertWithParameterlist() {

        LocalDateTime dateTime = LocalDateTime.of(2012, 8, 2, 11, 0);
        Instant instant = Instant.ofEpochSecond(150000);

        NamedParameterList list = new NamedParameterList()
                .with("name", "Doe")
                .with("first_name", "John")
                .with("number", 13)
                .with("registered", dateTime)
                .with("stamp", instant);

        insertInto("persons").value(list).execute();
        assertInsertedAux(dateTime, instant);
    }

    private void assertInsertedAux(LocalDateTime dateTime, Instant instant) {
        assertThat(
                select("name").from("persons where first_name = 'John'")
                        .getString()
        ).isEqualTo("Doe");
        assertThat(
                select("number").from("persons where first_name = 'John'")
                        .getInt()
        ).isEqualTo(13);
        assertThat(
                select("registered") .from("persons where first_name = 'John'")
                        .getLocalDateTime()
        ).isEqualTo(dateTime);
        assertThat(
                select("stamp") .from("persons where first_name = 'John'")
                        .getInstant()
        ).isEqualTo(instant);
    }

    @Test
    public void insertSharedStructure() {
        InsertSQLStatement statement = insertInto("persons")
                .value("name", "Doe");
        statement.value("first_name", "John")
                .execute();
        statement.value("first_name", "Bernard")
                .execute();
        assertThat(
                select("name").from("persons where first_name = 'Bernard'")
                        .getString()
        ).isEqualTo("Doe");
    }


    @Test
    public void generatedKey() {
        int key = insertInto("persons")
                .value("name", "Bond")
                .value("first_name", "James")
                .create();
        assertThat(key).isNotZero();
        assertThat(
                select("name").from("persons").where("id", key).getString()
        ).isEqualTo("Bond");
    }

    @Test
    public void insertWithGeneralSQLStatement() {
        LocalDateTime dateTime = LocalDateTime.of(2015, 5, 13, 11, 30);
        LocalDate date = dateTime.toLocalDate().plusDays(4);
        LocalTime time = dateTime.toLocalTime().plusMinutes(12);
        Instant instant = Instant.now().truncatedTo(ChronoUnit.MICROS);
        sql ("INSERT INTO persons(name,first_name,number,flag,registered,starts_work_at,birthday,stamp) " +
                "VALUES(?,?,?,?,?,?,?,?)")
                .parameter("Washington")
                .parameter("George")
                .parameter(15)
                .parameter(true)
                .parameter(dateTime)
                .parameter(time)
                .parameter(date)
                .parameter(instant)
                .execute();
        assertThat(
                select("name").from("persons where first_name = 'George'")
                        .getString()
        ).isEqualTo("Washington");
        assertThat(
                select("number").from("persons where first_name = 'George'")
                        .getInt()
        ).isEqualTo(15);
        assertThat(
                select("registered") .from("persons where first_name = 'George'")
                        .getLocalDateTime()
        ).isEqualTo(dateTime);
        assertThat(
                select("starts_work_at") .from("persons where first_name = 'George'")
                        .getLocalTime()
        ).isEqualTo(time);
        assertThat(
                select("birthday") .from("persons where first_name = 'George'")
                        .getLocalDate()
        ).isEqualTo(date);
        assertThat(
                select("stamp") .from("persons where first_name = 'George'")
                        .getInstant()
        ).isEqualTo(instant);
    }

    @Test(expected=IllegalArgumentException.class)
    public void insertWithoutValues () {
        insertInto("persons").execute();
    }

    @Test(expected=IllegalArgumentException.class)
    public void insertWithIncorrectNumberOfParameters () {
        sql ("INSERT INTO persons(name,first_name) VALUES(?,?)")
                .parameter("Doe")
                .parameter("John")
                .parameter(7).execute();
    }

    @Test
    public void generatedKeyNotFirstColumn() {
        int magicNr = 31415;
        int generatedId = insertInto("question").value("moduleId", magicNr).create("questionId");
        int questionId = select("questionId").from("question").where ("moduleId", magicNr).getInt();
        assertThat (
                generatedId
        ).isEqualTo(questionId);
        assertThat (
                select("moduleId").from("question").where("questionId", generatedId).getInt()
        ).isEqualTo(magicNr);
    }
}
