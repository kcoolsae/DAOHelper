/*
 * FullCoverageTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DataAccessException;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Additional tests to ensure (almost) full test coverage
 */
public class FullCoverageTest extends TestDAO {

    // Note: some of the key methods in UpsertHeader are not tested as they
    // need a table with the given type as primary key

    @Test
    public void insertWithParameters() {
        LocalDateTime dateTime = LocalDateTime.of(2014, 4, 1, 12, 0);
        Instant instant = Instant.ofEpochSecond(120000);
        insertOrUpdateInto("persons")
                .key("id", 13)
                .value("name", "Doe")
                .value("first_name", "John")
                .value("number", 13)
                .value("registered", dateTime)
                .value("stamp", instant)
                .value("flag", true)
                .value("birthday", LocalDate.of(1960, 1, 1))
                .value("starts_work_at", LocalTime.of(8, 30))
                .execute();
        assertInsertedAux(dateTime, instant);
    }

    @Test
    public void insertWithParameterList() {
        LocalDateTime dateTime = LocalDateTime.of(2014, 4, 1, 12, 0);
        Instant instant = Instant.ofEpochSecond(120000);
        NamedParameterList list = new NamedParameterList()
                .with("first_name", "John")
                .with("number", 13)
                .with("registered", dateTime)
                .with("stamp", instant)
                .with("flag", true)
                .with("birthday", LocalDate.of(1960, 1, 1))
                .with("starts_work_at", LocalTime.of(8, 30));
        insertOrUpdateInto("persons")
                .key("id", 13)
                .value("name", "Doe")
                .value(list)
                .execute();
        assertInsertedAux(dateTime, instant);
    }

    private void assertInsertedAux(LocalDateTime dateTime, Instant instant) {
        assertThat(
                select("name").from("persons").where("id", 13).getString()
        ).isEqualTo("Doe");
        assertThat(
                select("first_name").from("persons").where("id", 13).getString()
        ).isEqualTo("John");
        assertThat(
                select("number").from("persons").where("id", 13).getInt()
        ).isEqualTo(13);
        assertThat(
                select("registered").from("persons").where("id", 13).getLocalDateTime()
        ).isEqualTo(dateTime);
        assertThat(
                select("stamp").from("persons").where("id", 13).getInstant()
        ).isEqualTo(instant);
        assertThat(
                select("flag").from("persons").where("id", 13).getBoolean()
        ).isEqualTo(true);
        assertThat(
                select("birthday").from("persons").where("id", 13).getLocalDate()
        ).isEqualTo(LocalDate.of(1960, 1, 1));
        assertThat(
                select("starts_work_at").from("persons").where("id", 13).getLocalTime()
        ).isEqualTo(LocalTime.of(8, 30));

    }

    @Test(expected = IllegalArgumentException.class)
    public void namedParameterShouldBeSimple() {
        new NamedParameterList()
                .with("number > ?", 12);
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsInt() {
        select("unknown").from("persons").getInt();
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsBoolean() {
        select("unknown").from("persons").getBoolean();
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsObject() {
        select("unknown").from("persons").getObject(ResultSetConverter.FIRST_BOOLEAN);
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsOneObject() {
        select("unknown").from("persons").getOneObject(ResultSetConverter.FIRST_BOOLEAN);
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsList() {
        select("unknown").from("persons").getList(ResultSetConverter.FIRST_BOOLEAN);
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsConvert() {
        select("unknown").from("persons").convert(ResultSetConverter.FIRST_BOOLEAN);
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsMap() {
        select("unknown").from("persons").getMap(ResultSetConverter.FIRST_BOOLEAN);
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsProcessMap() {
        select("unknown").from("persons").processMap(
                Map.of("Key", "Value"),
                ResultSetConverter.FIRST_STRING,
                (s, rs) -> {
                }
        );
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsProcess() {
        select("unknown").from("persons").process(
                "destination",
                (s, rs) -> {
                }
        );
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsProcessConsumer() {
        select("unknown").from("persons").process(
                rs -> {
                }
        );
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsCreate() {
        insertInto("unknown").value("name", "John").create();
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsCreateBis() {
        insertInto("unknown").value("name", "John").create("name");
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsCall() {
        call("nonexistentProcedure").execute();
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsPage() {
        select("unknown").from("unknown")
                .orderBy("name")
                .onlyPage(1, 1)
                .getPage(ResultSetConverter.FIRST_BOOLEAN);
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsEmpty() {
        select("unknown").from("persons")
                .isEmpty();
    }

    @Test(expected = DataAccessException.class)
    public void catchesDatabaseErrorsUpdate() {
        update("unknown").set("number = number + 1").executeUpdate();
    }


}
