/*
 * SelectTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.AnswerNotUnique;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests select statements
 */
public class SelectTest extends TestDAO {

    private final static LocalDate BIRTHDAY = LocalDate.of(1983, 12, 26);
    private final static LocalTime STARTS_WORK_AT = LocalTime.of(8, 15);

    private int keyBond;
    private int keyDoe;

    @Before
    public void setup() {
        keyBond = insertInto("persons")
                .value("name", "Bond")
                .value("first_name", "James")
                .value("flag", false)
                .value("starts_work_at", STARTS_WORK_AT)
                .value("birthday", BIRTHDAY)
                .value("number", 7)
                .create();
        keyDoe = insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "Bernard")
                .value("flag", true)
                .value("registered", LocalDateTime.now())
                .value("stamp", Instant.ofEpochSecond(123456))
                .create();
    }

    @Test
    public void selectString() {
        assertThat(
                select("name").from("persons").where("id = ?", keyBond).getString()
        ).isEqualTo("Bond");
    }

    @Test
    public void selectInt() {
        assertThat(
                select("id").from("persons")
                        .where("name", "Bond")
                        .getInt()
        ).isEqualTo(keyBond);
    }

    @Test
    public void selectList() {
        assertThat(
                select("name,first_name").from("persons")
                        .getList(
                                rs -> rs.getString(1) + ", " + rs.getString(2)
                        )
        ).containsOnly("Bond, James", "Doe, Bernard");
    }

    @Test
    public void processList() {
        List<String> destination = new ArrayList<>();
        select("name").from("persons")
                .process(destination, (d, rs) -> d.add(rs.getString(1)));
        assertThat(
                destination
        ).containsExactlyInAnyOrder("Bond", "Doe");
    }

    @Test
    public void processListWithConsumer() {
        List<String> destination = new ArrayList<>();
        select("first_name").from("persons")
                .process(rs -> destination.add(rs.getString(1)));
        assertThat(
                destination
        ).containsExactlyInAnyOrder("James", "Bernard");
    }

    @Test
    public void selectOrderedList() {
        assertThat(
                select("id").from("persons")
                        .orderBy("name", true)
                        .getList(
                                rs -> rs.getInt(1)
                        )
        ).containsExactly(keyBond, keyDoe);
        assertThat(
                select("id").from("persons")
                        .orderBy("first_name", true)
                        .getList(
                                rs -> rs.getInt(1)
                        )
        ).containsExactly(keyDoe, keyBond);
    }

    private int sumOfIds(ResultSet rs) throws SQLException {
        int sum = 0;
        while (rs.next()) {
            sum += rs.getInt("id");
        }
        return sum;
    }

    @Test
    public void selectWithConvert() {
        assertThat(
                select("id").from("persons")
                        .orderBy("name", true)
                        .convert(this::sumOfIds)
        ).isEqualTo(keyBond + keyDoe);
    }

    @Test
    public void whereWithoutParameter() {
        assertThat(
                select("id").from("persons")
                        .where("name = 'Bond'")
                        .getInt()
        ).isEqualTo(keyBond);
    }

    @Test
    public void booleanWhere() {
        assertThat(
                select("id").from("persons")
                        .where("flag", true)
                        .getInt()
        ).isEqualTo(keyDoe);
    }

    @Test(expected = AnswerNotUnique.class)
    public void booleanNotUnique() {
        select("flag").from("persons").getOneBoolean();
    }

    @Test(expected = AnswerNotUnique.class)
    public void intNotUnique() {
        select("number").from("persons").getInt();
    }

    @Test(expected = AnswerNotUnique.class)
    public void stringNotUnique() {
        select("name").from("persons").getString();
    }

    @Test(expected = AnswerNotUnique.class)
    public void findIntNotUnique() {
        select("number").from("persons").findInt();
    }

    @Test(expected = AnswerNotUnique.class)
    public void findStringNotUnique() {
        select("name").from("persons").findString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongWhere() {
        select("id").from("persons")
                .where("name = ?")
                .getInt();
    }

    @Test
    public void whereTimes() {
        assertThat(
                select("id").from("persons").where("registered <= ?", LocalDateTime.now()).getOneInt()
        ).isEqualTo(keyDoe);
        assertThat(
                select("id").from("persons").where("starts_work_at", STARTS_WORK_AT).getOneInt()
        ).isEqualTo(keyBond);
        assertThat(
                select("id").from("persons").where("birthday", BIRTHDAY).getOneInt()
        ).isEqualTo(keyBond);
        assertThat(
                select("id").from("persons").where("stamp", Instant.ofEpochSecond(123456)).getOneInt()
        ).isEqualTo(keyDoe);
    }

    @Test
    public void withParameter() {
        assertThat(
                select("number + ?")
                        .parameter(13)
                        .from("persons")
                        .where("name", "Bond")
                        .getOneInt()
        ).isEqualTo(20);
    }

    @Test
    public void secondWhereWithoutParameter() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("name", "Bond")
                        .where("NOT flag")
                        .getOneInt()
        ).isEqualTo(7);

    }

    @Test
    public void multiWhere() {
        CompositeWhereClause comp = compositeWhereClause()
                .where (new WhereClause("name", new StringParameter("Bond")))
                .where (new WhereClause("NOT flag"));

        assertThat(
                select("number")
                        .from("persons")
                        .where(comp)
                        .getOneInt()
        ).isEqualTo(7);

    }

    @Test
    public void selectWithParameter() {
        insertInto("things")
                .value("id1", keyBond).value("id2", "romeo")
                .value("val1", "julia").value("val2", 88)
                .execute();
        insertInto("things")
                .value("id1", keyBond).value("id2", "julia")
                .value("val1", "romeo").value("val2", 99)
                .execute();
        assertThat(
                select("val2").from("persons JOIN things ON id = id1 AND val1 = ?")
                        .parameter("romeo")
                        .getOneInt()
        ).isEqualTo(99);
    }

    private StatementModifier nameAndFirstNameIs(String name, String firstName) {
        return compositeWhereClause()
                .where("name", name)
                .where("first_name", firstName);
    }

    @Test
    public void whereClauseBuilder() {
        assertThat(
                select("number")
                        .from("persons")
                        .where(nameAndFirstNameIs("Bond", "James"))
                        .getOneInt()
        ).isEqualTo(7);
    }

    @Test
    public void whereClauseBuilderMulti() {
        assertThat(
                select("number")
                        .from("persons")
                        .where(
                                compositeWhereClause()
                                        .where(new WhereClause("name", new StringParameter("Bond")))
                                        .where("first_name", "James")
                        )
                        .getOneInt()
        ).isEqualTo(7);
    }

        @Test
    public void namedParameters() {
        assertThat(
                select("number")
                        .from("persons")
                        .where(
                                new NamedParameterList()
                                        .with("name", "Bond")
                                        .with("first_name", "James")
                        )
                        .getOneInt()
        ).isEqualTo(7);
    }



}
