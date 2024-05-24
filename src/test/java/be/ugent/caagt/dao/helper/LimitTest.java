/*
 * LimitTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.Page;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test select statements with limits
 */
public class LimitTest extends TestDAO {

    @Before
    public void setup() {
        for (int i = 10; i < 23; i++) {
            insertInto("persons")
                    .value("name", "Name" + i)
                    .value("first_name", "Firstname" + i)
                    .value("number", i)
                    .execute();
        }
    }

    @Test
    public void limitedSelect() {
        assertThat(
                select("name").from("persons")
                        .orderBy("number")
                        .onlyPage(2, 5)
                        .getList(rs -> rs.getString(1))
        ).containsExactly("Name20", "Name21", "Name22");
    }

    @Test
    public void pagedSelect() {
        Page<String> page = select("name").from("persons")
                .orderBy("number")
                .onlyPage(1, 4)
                .getPage(rs -> rs.getString(1));
        assertThat(
                page.getList()
        ).containsExactly("Name14", "Name15", "Name16", "Name17");

        assertThat(page.getFullSize()).isEqualTo(13);
        assertThat(page.getPageSize()).isEqualTo(4);
        assertThat(page.getNrOfPages()).isEqualTo(4);
    }

    @Test
    public void pagedEmptyList() {
        Page<String> page = select("name")
                .from("persons")
                .where("number", 333)
                .orderBy("name").onlyPage(2,20).getPage(rs -> rs.getString(1));
        assertThat(page.getList()).isEmpty();
        assertThat(page.getFullSize()).isZero();
    }
}
