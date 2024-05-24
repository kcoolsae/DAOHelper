/*
 * MapTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests query results that generate or manipulate maps and upserts.
 */
public class MapTest extends TestDAO {

    private int keyBond;

    private int keyDoe;

    @Before
    public void setup() {
        keyBond = insertInto("persons")
                .value("name", "Bond")
                .create();
        keyDoe = insertInto("persons")
                .value("name", "Doe").create();

        InsertSQLStatement statBond = insertInto("details").value("id", keyBond);
        statBond.value("nr", 4).execute();
        statBond.value("nr", 7).execute();
        InsertSQLStatement statDoe = insertInto("details").value("id", keyDoe);
        statDoe.value("nr", 5).execute();
        statDoe.value("nr", 9).execute();

        statBond.value("nr", 12).execute();
    }

    private static class PersonDetails {
        public final String name;
        public final List<Integer> details;

        PersonDetails(String name) {
            this.name = name;
            this.details = new ArrayList<>();
        }
    }

    @Test
    public void getMap() {
        Map<Integer, PersonDetails> map = select("id, name").from("persons").orderBy("name", false)
                .getMap(rs -> new PersonDetails(rs.getString("name")));
        assertThat(
                map
        ).containsKeys (keyBond, keyDoe);

        assertThat(
                map.keySet()
        ).containsExactly(keyDoe, keyBond);

        assertThat(
                map.get(keyDoe).name
        ).isEqualTo("Doe");
    }

    @Test
    public void masterDetail() {
        Map<Integer, PersonDetails> map = select("id, name").from("persons")
                .orderBy("name", false)
                .getMap(rs -> new PersonDetails(rs.getString("name"))
                );
        select("id, nr").from("details").orderBy("details").processMap(
                map,
                (obj, rs) -> obj.details.add(rs.getInt("nr"))
        );
        assertThat(
                map.get(keyBond).details
        ).containsExactly(4, 7, 12);
    }
}
