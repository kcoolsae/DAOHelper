/*
 * EnumTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.DataAccessException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumTest extends TestDAO {

    enum Color {
        RED, GREEN, BLUE
    }

    @Before
    public void setup() {
        insertInto("colors")
                .value("id", 13)
                .value("color", Color.BLUE)
                .execute();
    }

    @Test
    public void get() {
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isEqualTo(Color.BLUE);
        assertThat(
                select("id").from("colors").where("color", Color.BLUE).getInt()
        ).isEqualTo(13);
    }

    @Test
    public void find() {
        assertThat(
                select("color").from("colors").where("id", 13).findEnum(Color.class)
        ).isPresent().contains(Color.BLUE);
        assertThat(
                select("color").from("colors").where("id", 31).findEnum(Color.class)
        ).isNotPresent();
    }

    @Test
    public void update() {
        update("colors").set("color", Color.RED).where("id", 13).execute();
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isEqualTo(Color.RED);
    }

    @Test
    public void upsert() {
        for (int id = 13; id < 15; id++) {
            insertOrUpdateInto("colors")
                    .key("id", id)
                    .value("color", Color.GREEN)
                    .execute();
            assertThat(
                    select("color").from("colors").where("id", id).getEnum(Color.class)
            ).isEqualTo(Color.GREEN);
        }
        insertOrUpdateInto("colorNames")
                .key("color", Color.BLUE)
                .value("name", "Blueish")
                .execute();
        assertThat(
                select("name").from("colorNames").where("color", Color.BLUE).getString()
        ).isEqualTo("Blueish");
    }

    @Test
    public void insertNull() {
        insertInto("colors")
                .value("id", 14)
                .value("color", (Color) null)
                .execute();
        assertThat(
                select("color").from("colors").where("id", 14).getEnum(Color.class)
        ).isNull();
        assertThat(
                select("1").from("colors").where("id", 14).where("color IS NULL").isEmpty()
        ).isFalse();
        assertThat(
                select("color").from("colors").where("id", 14).findEnum(Color.class)
        ).isNotPresent();
    }

    @Test
    public void updateNull() {
        update("colors").set("color", (Color) null).where("id", 13).execute();
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isNull();
    }

    @Test
    public void upsertNull() {
        insertOrUpdateInto("colors")
                .key("id", 13)
                .value("color", (Color) null)
                .execute();
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isNull();
        insertOrUpdateInto("colors")
                .key("id", 13)
                .value("color", Color.RED)
                .execute();
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isEqualTo(Color.RED);
    }

    enum Shape {
        CIRCLE {
            @Override
            public String toString() {
                return "circle";
            }
        }
    }

    @Test
    public void plainQuestionMark() {
        // same SQL for null and non-null values, and for constants with a body
        assertThat(
                new EnumParameter<>(Color.RED).getQuestionMark()
        ).isEqualTo("?");
        assertThat(
                new EnumParameter<>((Color) null).getQuestionMark()
        ).isEqualTo("?");
        assertThat(
                new EnumParameter<>(Shape.CIRCLE).getQuestionMark()
        ).isEqualTo("?");
    }

    /**
     * Java enum type with a name that differs from the database type, and with
     * constants that have a body and a different string representation.
     */
    enum Paint {
        RED {
            @Override
            public String toString() {
                return "rood";
            }
        },
        GREEN,
        BLUE,
        YELLOW // not in the database
    }

    @Test
    public void typeInferredFromContext() {
        insertInto("colors").value("id", 15).value("color", Paint.RED).execute();
        assertThat(
                select("color").from("colors").where("id", 15).getEnum(Color.class)
        ).isEqualTo(Color.RED);
        assertThat(
                select("color").from("colors").where("id", 15).getEnum(Paint.class)
        ).isEqualTo(Paint.RED);
        assertThat(
                select("id").from("colors").where("color", Paint.RED).getInt()
        ).isEqualTo(15);
    }

    @Test(expected = DataAccessException.class)
    public void unknownConstant() {
        insertInto("colors").value("id", 15).value("color", Paint.YELLOW).execute();
    }

    @Test
    public void userWrittenQuestionMark() {
        assertThat(
                select("id").from("colors").where("color = ?", Color.BLUE).getInt()
        ).isEqualTo(13);
        assertThat(
                select("id").from("colors").where("color IN (?, ?)", Color.RED).parameter(Color.BLUE).getInt()
        ).isEqualTo(13);
        sql("UPDATE colors SET color = ? WHERE id = ?").parameter(Color.GREEN).parameter(13).execute();
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isEqualTo(Color.GREEN);
    }

    @Test
    public void setWithQuestionMark() {
        update("colors").set("color = COALESCE(?, color)", (Color) null).where("id", 13).execute();
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isEqualTo(Color.BLUE);
        update("colors").set("color = COALESCE(?, color)", Color.RED).where("id", 13).execute();
        assertThat(
                select("color").from("colors").where("id", 13).getEnum(Color.class)
        ).isEqualTo(Color.RED);
    }

    @Test
    public void selectList() {
        assertThat(
                select("color = ?").parameter(Color.BLUE).from("colors").where("id", 13).getOneBoolean()
        ).isTrue();
        // without context the parameter is treated as text
        assertThat(
                select("?").parameter(Color.RED).noFrom().getString()
        ).isEqualTo("RED");
        assertThat(
                select("?").parameter(Color.RED).noFrom().getEnum(Color.class)
        ).isEqualTo(Color.RED);
    }

    @Test
    public void textColumn() {
        insertInto("persons").value("name", Color.GREEN).value("number", 77).execute();
        assertThat(
                select("number").from("persons").where("name", Color.GREEN).getInt()
        ).isEqualTo(77);
    }

    @Test
    public void compositeWhere() {
        assertThat(
                select("id").from("colors")
                        .where(compositeWhereClause()
                                .where("color", Color.BLUE)
                                .where("id > ?", 10))
                        .getInt()
        ).isEqualTo(13);
    }

    @Test
    public void withNamedParameterList() {
        assertThat(
                select("id").from("colors")
                        .where(new NamedParameterList().with("color", Color.BLUE))
                        .getInt()
        ).isEqualTo(13);
        insertOrUpdateInto("colorNames")
                .key(new NamedParameterList().with("color", Color.GREEN))
                .value("name", "Greenish")
                .execute();
        assertThat(
                select("name").from("colorNames").where("color", Color.GREEN).getString()
        ).isEqualTo("Greenish");
    }

    @Test
    public void having() {
        assertThat(
                select("count(*)").from("colors")
                        .groupBy("color")
                        .having("color", Color.BLUE)
                        .getInt()
        ).isEqualTo(1);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedTranslatorStillAccepted() {
        assertThat(
                select("id").from("colors")
                        .where(new CompositeWhereClause(EnumClassNameTranslator.NO_OP).where("color", Color.BLUE))
                        .where(new WhereClause("color = ?", Color.BLUE, EnumClassNameTranslator.SNAKE_CASE))
                        .getInt()
        ).isEqualTo(13);
        assertThat(
                new EnumParameter<>(Color.RED, EnumClassNameTranslator.SNAKE_CASE).getQuestionMark()
        ).isEqualTo("?");
    }
}
