/*
 * DoubleTest.java
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

/**
 * Tests various operations that use doubles
 */
public class DoubleTest extends TestDAO {

    @Before
    public void setup() {
        // 1.0 2.0 3.0
        // 2.0 4.0 4.0
        // 3.0 6.0 4.0
        // 4.0 8.0 5.0
        for (int i=1; i <= 4; i++) {
            insertInto("coordinates")
                    .value("x", 1.0 * i)
                    .value("y", 2.0 * i)
                    .value("z", 3.0 + (double)(i/2))
                    .execute();
        }
    }

    @Test
    public void select() {
        assertThat (
                select("y").from("coordinates").where ("z", 5.0).getDouble()
        ).isEqualTo (8.0);
        assertThat (
                select("y").from("coordinates").where ("z", 5.0).getOneDouble()
        ).isEqualTo (8.0);
        assertThat (
                select("y").from("coordinates").where ("z", 5.0).findDouble()
        ).isPresent().hasValue(8.0);
    }

    @Test
    public void groupBy() {
        assertThat(
                select("count(*)").from("coordinates").groupBy("z").having("z", 4.0).getInt()
        ).isEqualTo(2);
    }

    @Test
    public void insertSpecialValues() {
        insertOrUpdateInto("coordinates")
                .key ("x", 1.0)
                .value("y", Double.NEGATIVE_INFINITY)
                .value("z", Double.POSITIVE_INFINITY)
                .execute();
        assertThat (
                select("y").from("coordinates").where("x", 1.0)
                        .getOneDouble()
        ).isEqualTo(Double.NEGATIVE_INFINITY);
        assertThat (
                select("z").from("coordinates").where("x", 1.0)
                        .getOneDouble()
        ).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void insertSpecialValues2() {
        insertInto("coordinates")
                .value("x", 5.0)
                .value(new NamedParameterList()
                        .with("y", Double.NaN)
                        .with( "z", -0.0)
                ).execute();
        assertThat(
                Double.isNaN(select ("y").from("coordinates").where("x", 5.0)
                        .getDouble())
        ).isTrue();
        assertThat(
                select ("z").from("coordinates").where("x", 5.0)
                        .getDouble()
        ).isEqualTo(-0.0); // TODO is this the correct test?

    }

    @Test
    public void test1() {
        update("coordinates").set("y", 2.0).where("x", 2.0).execute();
        sql("UPDATE coordinates SET z=2.0 WHERE x=?").parameter(2.0).execute();
        assertThat(
                select("y").from("coordinates").where("x", 2.0).getDouble()
        ).isEqualTo(2.0);
        assertThat(
                select("z").from("coordinates").where("x", 2.0).getDouble()
        ).isEqualTo(2.0);

    }
}
