/*
 * ArrayParameterTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayParameterTest extends TestDAO {

    @Test
    public void string() {
        assertThat(
                select ("max(name)")
                        .from("unnest(?::text[]) as name")
                        .parameter(new String[] {"John", "Doe"})
                        .getString()
        ).isEqualTo("John");
        assertThat(
                select ("min(name)")
                        .from("unnest(?::text[]) as name")
                        .parameter(new String[][] {{"John", "Doe"}, {"Alice", "Bob"}})
                        .getString()
        ).isEqualTo("Alice");
    }

    @Test
    public void ints() {
        assertThat(
                select ("max(number)")
                        .from("unnest(?::int[]) as number")
                        .parameter(new int[] {3, 4, 5, 6, 7})
                        .getInt()
        ).isEqualTo(7);
        assertThat(
                select ("max(number)")
                        .from("unnest(?::int[]) as number")
                        .parameter(new Integer[] {3, 4, 5, 6, 7})
                        .getInt()
        ).isEqualTo(7);
        assertThat(
                select ("min(number)")
                        .from("unnest(?::int[]) as number")
                        .parameter(new int[][] {{3, 4, 5}, {8, 6, 7}, {1, 2, 3}})
                        .getInt()
        ).isEqualTo(1);
        assertThat(
                select ("min(number)")
                        .from("unnest(?::int[]) as number")
                        .parameter(new Integer[][] {{3, 4, 5}, {8, 6, 7}, {1, 2, 3}})
                        .getInt()
        ).isEqualTo(1);
    }

    @Test
    public void doubles() {
        assertThat(
                select ("max(number)")
                        .from("unnest(?::float8[]) as number")
                        .parameter(new double[] {3.0, 4.0, 5.0, 6.0, 7.0})
                        .getDouble()
        ).isEqualTo(7.0);
        assertThat(
                select ("max(number)")
                        .from("unnest(?::float8[]) as number")
                        .parameter(new Double[] {3.0, 4.0, 5.0, 6.0, 7.0})
                        .getDouble()
        ).isEqualTo(7.0);
        assertThat(
                select ("min(number)")
                        .from("unnest(?::float8[]) as number")
                        .parameter(new double[][] {{3.0, 4.0, 5.0}, {8.0, 6.0, 7.0}, {1.0, 2.0, 3.0}})
                        .getDouble()
        ).isEqualTo(1.0);
        assertThat(
                select ("min(number)")
                        .from("unnest(?::int[]) as number")
                        .parameter(new Double[][] {{3.0, 4.0, 5.0}, {8.0, 6.0, 7.0}, {1.0, 2.0, 3.0}})
                        .getDouble()
        ).isEqualTo(1.0);
    }
}
