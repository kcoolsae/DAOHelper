/*
 * ArrayWhereTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayWhereTest extends FGTestBase {

    @Test
    public void intArray() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("number = ANY(?)", new int[]{3, 4, 5, 6, 7})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(7, 6);
    }

        @Test
    public void intArray2() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("number = ANY(?)", new Integer[] {3, 4, 5, 6, 7})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(7, 6);
    }


    @Test
    public void doubleArray() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("number < ALL(?)", new double[]{6.5, 7.5, 8.5})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(6);
    }

    @Test
    public void doubleArray2() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("number < ALL(?)", new Double[] {6.5, 7.5, 8.5})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(6);
    }

    @Test
    public void stringArray() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("first_name = ANY(?)", new String[]{"Alice", "Bob", "Charlie", "James"})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(7);
    }

    @Test
    public void int2dimArray() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("number = ANY(?)", new int[][]{{3, 4}, {5, 6}, {7,8}})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(7, 6);
        assertThat(
                select("number")
                        .from("persons")
                        .where("number = ANY(?)", new Integer[][]{{3, 4}, {5, 6}, {7,8}})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(7, 6);
    }

    @Test
    public void double2dimArray() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("number < ALL(?)", new double[][]{{6.5, 7.5}, {8.5, 9.5}})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(6);
        assertThat(
                select("number")
                        .from("persons")
                        .where("number < ALL(?)", new Double[][]{{6.5, 7.5}, {8.5, 9.5}})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(6);

    }

        @Test
    public void string2dimArray() {
        assertThat(
                select("number")
                        .from("persons")
                        .where("first_name = ANY(?)", new String[][]{{"Alice", "Bob"}, {"Charlie", "James"}})
                        .getList(ResultSetConverter.FIRST_INTEGER)
        ).containsOnly(7);
    }


}
