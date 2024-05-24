/*
 * GroupedTest.java
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests 'group by' statements
 */
public class GroupedTest extends TestDAO {

    @Before
    public void setup() {
        Instant instant = Instant.now();
        for (int i = 10; i < 20; i++) {
            insertInto("persons")
                    .value("name", "Bond" + i % 5)
                    .value("first_name", "James")
                    .value("flag", i % 2 == 1)
                    .value("starts_work_at", LocalTime.of(15, i / 5 * 5))
                    .value("birthday", LocalDate.of(1980 + i / 4 * 4, 4, 1))
                    .value("registered", LocalDateTime.of(2016, 5, 1 + i % 10, 14, 0))
                    .value("stamp", instant)
                    .value("number", i / 3 * 3)
                    .execute();
        }
    }

    @Test
    public void groupBy() {
        assertThat(
                select("number").from("persons")
                        .groupBy("number")
                        .orderBy("number", true)
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsExactly(9, 12, 15, 18);
    }

    @Test
    public void groupByWhere() {
        assertThat(
                select("number").from("persons")
                        .where("name", "Bond0")
                        .groupBy("number")
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(9, 15);
    }

    @Test
    public void groupByHaving() {
        assertThat(
                select("number").from("persons")
                        .groupBy("number")
                        .having("number < ?", 15)
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(9, 12);
    }

    @Test
    public void groupByHaving2() {
        assertThat(
                select("max(number)").from("persons")
                        .groupBy("name")
                        .having("name", "Bond0")
                        .getOneInt()
        ).isEqualTo(15);
        assertThat(
                select("max(number)").from("persons")
                        .groupBy("name")
                        .having("name = 'Bond0'")
                        .getOneInt()
        ).isEqualTo(15);
        assertThat(
                select("min(number)").from("persons")
                        .groupBy("flag")
                        .having("flag", true)
                        .getOneInt()
        ).isEqualTo(9);
    }

    @Test
    public void groupByHavingTime() {
        assertThat(
                select("count(id)").from("persons")
                        .groupBy("stamp").having("stamp <= ?", Instant.now())
                        .getOneInt()
        ).isEqualTo(10);
        assertThat(
                select("count(id)").from("persons")
                        .groupBy("registered").having("registered", LocalDateTime.of(2016, 5, 3, 14, 0))
                        .getOneInt()
        ).isEqualTo(1);
        assertThat(
                select("birthday, starts_work_at").from("persons")
                        .groupBy("birthday, starts_work_at")
                        .having("starts_work_at", LocalTime.of(15, 5))
                        .having("birthday", LocalDate.of(1984, 4, 1))
                        .getList(ResultSetConverter.FIRST_LOCAL_DATE)
        ).isEmpty();
    }

        @Test
    public void groupByHavingAux() {
        assertThat(
                select("count(id)").from("persons")
                        .groupBy("name, number")
                        .having("name", "Bond0")
                        .having("number >= ?", 0)
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsExactly(1, 1);
    }

    @Test
    public void groupByHavingComposite() {
        assertThat(
                select("count(id)").from("persons")
                        .groupBy("name, number")
                        .having(
                                compositeWhereClause()
                                        .where("name", "Bond0")
                                        .where("number >= ?", 0)
                        )
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsExactly(1, 1);
    }

    @Test
    public void groupByHavingParameters() {
        assertThat(
                select("count(id)").from("persons")
                        .groupBy("name, number")
                        .having(
                                new NamedParameterList()
                                        .with("name", "Bond0")
                                        .with("number", 9)
                        )
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsExactly(1);
    }

}
