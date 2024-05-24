/*
 * ArrayTest.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayTest extends TestDAO {

    private final String[] STRING_ARRAY = {"alfa", "beta"};

    private final int[] INT_ARRAY = {2, 4, 6};

    private final Integer[] INTEGER_ARRAY = {3, 5, 8};

    private final double[] DOUBLE_ARRAY = {3.0, 5.0};

    private final Double[] WRAPPED_DOUBLE_ARRAY = {4.0, 6.0};

    @Before
    public void setup() {
        insertInto("arrayTable")
                .value("id", 1)
                .value("strs", STRING_ARRAY)
                .execute();
        insertInto("arrayTable")
                .value("id", 2)
                .value("ints", INTEGER_ARRAY)
                .execute();
        insertInto("arrayTable")
                .value("id", 3)
                .value("ints", INT_ARRAY)
                .execute();
        insertInto("arrayTable")
                .value("id", 4)
                .value("ints", (int[]) null)
                .execute();
        insertInto("arrayTable")
                .value("id", 5)
                .value("doubles", DOUBLE_ARRAY)
                .execute();
        insertInto("arrayTable")
                .value("id", 6)
                .value("doubles", WRAPPED_DOUBLE_ARRAY)
                .execute();
        insertInto("arrayTable")
                .value("id", 7)
                .value("ints", new int[][]{{1, 2}, {3, 4}})
                .value("doubles", new double[][]{{100.0, 200.0}, {300.0, 400.0}})
                .execute();
        insertInto("arrayTable")
                .value("id", 8)
                .value("ints", new Integer[][]{{-1, -2}, {-3, -4}})
                .value("doubles", new Double[][]{{-100.0, -200.0}, {-300.0, -400.0}})
                .value("strs", new String[][]{{"a", "b", "c"}, {"d", "e", "f"}})
                .execute();
    }

    @Test
    public void insert() {
        assertThat(select("strs[1]").from("arrayTable").where("id", 1)
                .getString()).isEqualTo("alfa");
        assertThat(select("strs[2]").from("arrayTable").where("id", 1)
                .getString()).isEqualTo("beta");
        assertThat(select("ints[3]").from("arrayTable").where("id", 2)
                .getInt()).isEqualTo(8);
        assertThat(select("ints[2]").from("arrayTable").where("id", 3)
                .getInt()).isEqualTo(4);
        assertThat(select("doubles[1]").from("arrayTable").where("id", 5)
                .getDouble()).isEqualTo(3.0);
        assertThat(select("doubles[2]").from("arrayTable").where("id", 6)
                .getDouble()).isEqualTo(6.0);
        assertThat(select("ints[2][2]").from("arrayTable").where("id", 7)
                .getInt()).isEqualTo(4);
        assertThat(select("doubles[2][2]").from("arrayTable").where("id", 7)
                .getDouble()).isEqualTo(400.0);
        assertThat(select("ints[2][2]").from("arrayTable").where("id", 8)
                .getInt()).isEqualTo(-4);
        assertThat(select("doubles[2][2]").from("arrayTable").where("id", 8)
                .getDouble()).isEqualTo(-400.0);
        assertThat(select("strs[2][3]").from("arrayTable").where("id", 8)
                .getString()).isEqualTo("f");
    }

    @Test
    public void update() {
        update("arrayTable")
                .set("strs", new String[]{"gamma", "delta", "epsilon"}).where("id", 1)
                .executeUpdate();
        assertThat(select("strs[1]").from("arrayTable").where("id", 1)
                .getString()).isEqualTo("gamma");
        assertThat(select("strs[3]").from("arrayTable").where("id", 1)
                .getString()).isEqualTo("epsilon");
    }

    @Test
    public void updateInts() {
        update("arrayTable")
                .set("ints", new int[]{13, 10}).where("id", 2)
                .executeUpdate();
        assertThat(select("ints[1]").from("arrayTable").where("id", 2)
                .getInt()).isEqualTo(13);
        update("arrayTable")
                .set("ints", new int[]{23, 20}).where("id", 3)
                .executeUpdate();
        assertThat(select("ints[2]").from("arrayTable").where("id", 3)
                .getInt()).isEqualTo(20);
        update("arrayTable")
                .set("ints", new Integer[][]{{-15, -16}}).where("id", 8)
                .executeUpdate();
        assertThat(select("ints[1][2]").from("arrayTable").where("id", 8)
                .getInt()).isEqualTo(-16);
    }

    @Test
    public void updateDoubles() {
        update("arrayTable")
                .set("doubles", new double[]{12.0, 17.0}).where("id", 5)
                .executeUpdate();
        assertThat(select("doubles[1]").from("arrayTable").where("id", 5)
                .getDouble()).isEqualTo(12.0);
        update("arrayTable")
                .set("doubles", new Double[]{23.5, 20.5, 1.5, 17.0}).where("id", 6)
                .executeUpdate();
        assertThat(select("doubles[4]").from("arrayTable").where("id", 6)
                .getDouble()).isEqualTo(17.0);
        update("arrayTable")
                .set("doubles", new double[][]{{23.5}, {20.5}, {1.5}}).where("id", 7)
                .executeUpdate();
        assertThat(select("doubles[3][1]").from("arrayTable").where("id", 7)
                .getDouble()).isEqualTo(1.5);
        update("arrayTable")
                .set("doubles", new Double[][]{{-23.5}, {-20.5}, {-1.5}}).where("id", 8)
                .executeUpdate();
        assertThat(select("doubles[3][1]").from("arrayTable").where("id", 8)
                .getDouble()).isEqualTo(-1.5);
    }

    @Test
    public void getArray() {
        assertThat(select("strs").from("arrayTable").where("id", 1)
                .getArrayOfString()).containsExactly(STRING_ARRAY);
        assertThat(select("ints").from("arrayTable").where("id", 2)
                .getArrayOfInteger()).containsExactly(INTEGER_ARRAY);
        assertThat(select("ints").from("arrayTable").where("id", 3)
                .getArrayOfInt()).containsExactly(INT_ARRAY);
        assertThat(select("doubles").from("arrayTable").where("id", 6)
                .getArrayOfWrappedDouble()).containsExactly(WRAPPED_DOUBLE_ARRAY);
        assertThat(select("doubles").from("arrayTable").where("id", 5)
                .getArrayOfDouble()).containsExactly(DOUBLE_ARRAY);

        double[][] doubles = select("doubles").from("arrayTable").where("id", 7).get2DimArrayOfDouble();
        assertThat (doubles[0][0]).isEqualTo(100.0);
        assertThat (doubles[0][1]).isEqualTo(200.0);
        assertThat (doubles[1][0]).isEqualTo(300.0);
        assertThat (doubles[1][1]).isEqualTo(400.0);

        String[][] strs = select("strs").from ("arrayTable").where("id", 8).get2DimArrayOfString();
        assertThat (strs[0]).containsExactly("a", "b", "c");
        assertThat (strs[1]).containsExactly("d", "e", "f");
    }

    @Test
    public void getNullArray() {
        assertThat(select("ints").from("arrayTable").where("id", 4)
                .getArrayOfInt()).isNull();
    }

    @Test
    public void test2DimArray() {
        sql("INSERT INTO arrayTable(id,ints) VALUES (?,'{{1,2},{3,4}}')")
                .parameter(100)
                .execute();
        assertThat(select("ints[2][2]").from("arrayTable").where("id", 100).getInt())
                .isEqualTo(4);
        int[][] result = select("ints").from("arrayTable").where("id", 100).get2DimArrayOfInt();
        assertThat(result[0][0]).isEqualTo(1);
        assertThat(result[0][1]).isEqualTo(2);
        assertThat(result[1][0]).isEqualTo(3);
        assertThat(result[1][1]).isEqualTo(4);
    }
}
