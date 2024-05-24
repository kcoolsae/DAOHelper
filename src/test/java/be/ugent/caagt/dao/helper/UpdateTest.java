/*
 * UpdateTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test update operations
 */
public class UpdateTest extends TestDAO {

    private int key;

    @Before
    public void setup() {
        this.key = insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "John")
                .value("number", 13)
                .value("flag", true)
                .create();
    }

    private void checkJohnBecameJane() {
        assertThat(
                select("first_name").from("persons").where("id = ?", key).getString()
        ).isEqualTo("Jane");
    }

    @Test
    public void simpleUpdate() {
        update("persons").set("first_name", "Jane").where("id", key).execute();
        checkJohnBecameJane();
    }

    private void checkNumberBecame17() {
        assertThat(
                select("number").from("persons").where("id", key).getInt()
        ).isEqualTo(17);
    }

    private void checkFlagBecameFalse() {
        assertThat(
                select("flag").from("persons").where("id", key).getOneBoolean()
        ).isFalse();
    }

    @Test
    public void updateWithDoubleSet() {
        update("persons")
                .set("first_name", "Jane")
                .set("number", 17)
                .set("flag", false)
                .where("id", key).execute();
        checkJohnBecameJane();
        checkNumberBecame17();
        checkFlagBecameFalse();
    }

    @Test
    public void updateLocalDateTime() {
        assertThat(
                select("registered").from("persons").where("id", key).getOneLocalDateTime()
        ).isNull();
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        update("persons")
                .set("registered", dateTime)
                .where("id", key)
                .execute();
        assertThat(
                select("registered").from("persons").where("id", key).getOneLocalDateTime()
        ).isEqualTo(dateTime);
        update("persons")
                .set("registered", (LocalDateTime) null)
                .where("id", key)
                .execute();
        assertThat(
                select("registered").from("persons").where("id", key).getOneLocalDateTime()
        ).isNull();
    }

    @Test
    public void updateLocalDate() {
        assertThat(
                select("birthday").from("persons").where("id", key).getOneLocalDate()
        ).isNull();
        LocalDate date = LocalDate.now();
        update("persons")
                .set("birthday", date)
                .where("id", key)
                .execute();
        assertThat(
                select("birthday").from("persons").where("id", key).getOneLocalDate()
        ).isEqualTo(date);
        update("persons")
                .set("birthday", (LocalDate) null)
                .where("id", key)
                .execute();
        assertThat(
                select("birthday").from("persons").where("id", key).getOneLocalDate()
        ).isNull();
    }

    @Test
    public void updateLocalTime() {
        assertThat(
                select("starts_work_at").from("persons").where("id", key).getOneLocalTime()
        ).isNull();
        LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.MICROS);
        update("persons")
                .set("starts_work_at", time)
                .where("id", key)
                .execute();
        assertThat(
                select("starts_work_at").from("persons").where("id", key).getOneLocalTime()
        ).isEqualToIgnoringNanos(time);   // database does not store milli or nanoseconds
        update("persons")
                .set("starts_work_at", (LocalTime) null)
                .where("id", key)
                .execute();
        assertThat(
                select("starts_work_at").from("persons").where("id", key).getOneLocalTime()
        ).isNull();
    }

    @Test
    public void updateInstant() {
        assertThat(
                select("stamp").from("persons").where("id", key).getOneInstant()
        ).isNull();
        Instant instant = Instant.now().truncatedTo(ChronoUnit.MICROS);
        update("persons")
                .set("stamp", instant)
                .where("id", key)
                .execute();
        assertThat(
                select("stamp").from("persons").where("id", key).getOneInstant()
        ).isEqualTo(instant);
        update("persons")
                .set("stamp", (Instant) null)
                .where("id", key)
                .execute();
        assertThat(
                select("stamp").from("persons").where("id", key).getOneInstant()
        ).isNull();
    }

    @Test
    public void updateWithDoubleWhere() {
        update("persons")
                .set("first_name", "Jane")
                .where("name", "Doe")
                .where("id", key)
                .execute();
        checkJohnBecameJane();
    }

    @Test
    public void multiWhere() {

        update("persons")
                .set("first_name", "Jane")
                .where(compositeWhereClause()
                        .where("name", "Doe")
                        .where("id", key))
                .execute();
        checkJohnBecameJane();

    }

    @Test
    public void updateWithGeneralStatement() {
        sql("UPDATE persons SET first_name = ? WHERE id = ?")
                .parameter("Jane")
                .parameter(key)
                .execute();
        checkJohnBecameJane();
    }

    @Test
    public void updateIncrement() {
        update("persons")
                .set("number = number + ?", 1)
                .where("id = ?", key)
                .execute();
        assertThat(select("number").from("persons").where("id", key).getInt()).isEqualTo(14);
    }

    @Test
    public void updateIncrementSingleParameter() {
        update("persons")
                .set("number = number + 1")
                .set("flag = FALSE")
                .where("id = ?", key)
                .execute();
        assertThat(select("number").from("persons").where("id", key).getInt()).isEqualTo(14);
        assertThat(select("flag").from("persons").where("id", key).getBoolean()).isEqualTo(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWrong() {
        update("persons")
                .set("number = number + ?")  // ? is forbidden when there is no argument
                .where("id = ?", key)
                .execute();
    }

    @Test
    public void updateWithMultiSet() {
        update("persons")
                .set("first_name", "Jane")
                .set(new NamedParameterList()
                        .with("number", 17)
                        .with("flag", false))
                .where("id", key).execute();
        checkJohnBecameJane();
        checkNumberBecame17();
        checkFlagBecameFalse();
    }
}
