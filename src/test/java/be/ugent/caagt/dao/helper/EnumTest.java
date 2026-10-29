/*
 * EnumTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

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
                return "CIRCLE";
            }
        }
    }

    @Test
    public void constantWithBody() {
        assertThat(
                new EnumParameter<>(Shape.CIRCLE, EnumClassNameTranslator.SNAKE_CASE).getQuestionMark()
        ).isEqualTo("?::shape");
    }

}
