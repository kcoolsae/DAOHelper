/*
 * IntersectionTest.java
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IntersectionTest extends TestDAO {

    @Before
    public void setup() {
        insertInto("details").value("id", 2).value("nr", 7).execute();
        insertInto("details").value("id", 2).value("nr", 6).execute();
        insertInto("details").value("id", 3).value("nr", 5).execute();
        insertInto("details").value("id", 3).value("nr", 4).execute();
        insertInto("details").value("id", 4).value("nr", 3).execute();
        insertInto("details").value("id", 5).value("nr", 2).execute();
        insertInto("stuff").value("id", 3)
                .value("val1", "v1")
                .value("val2", 17).execute();
    }

    @Test
    public void test1() {
        assertThat(
                intersection(
                        select("id").from("details").where("id > 3"),
                        select("id").from("details").where("nr < 5")
                ).orderBy("id", false).getList(rs -> rs.getInt("id"))
        ).contains(5, 4);
    }

    @Test
    public void test2() {
        assertThat(
                intersection(
                        select("nr").from("details").where("id >= ?", 4),
                        select("id").from("stuff").where("val2", 17)
                ).getList(rs -> rs.getInt(1))
        ).contains(3);
    }

    @Test
    public void testPaging() {
        LimitedSQLStatement limitedSQLStatement = intersection(
                select("id").from("details").where("id > 3"),
                select("id").from("details").where("nr < 5")
        ).orderBy("id", false).onlyPage(1, 1);
        assertThat(
                limitedSQLStatement.getList(rs -> rs.getInt("id"))
        ).contains(4);
        Page<Integer> page = limitedSQLStatement.getPage(ResultSetConverter.FIRST_INTEGER);
        assertThat(page.getList()).contains(4);
        assertThat(page.getNrOfPages()).isEqualTo(2);
    }

    @Test
    public void testPagingSingleSelect() {
        assertThat(intersection(
                        select("nr").from("details").where("id > 2")
                ).orderBy("id", true).onlyPage(0, 2).getPage(rs -> rs.getInt("nr")).getList()
        ).containsExactly(5, 4);
    }

    @Test
    public void specialCases() {
        assertThat(
                intersection(
                        select("id").from("details").where("nr >= ?", 4)
                ).getList(rs -> rs.getInt("id"))
        ).contains(2, 3);
        assertThatThrownBy(() -> intersection(List.of())).isInstanceOf(IllegalArgumentException.class);
    }
}
