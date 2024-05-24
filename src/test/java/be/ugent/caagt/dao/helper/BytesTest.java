/*
 * BytesTest.java
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

import static org.assertj.core.api.Assertions.assertThat;

public class BytesTest extends TestDAO {

    private static final String CODE_STRING = "opensesame";
    private static final byte[] CODE = CODE_STRING.getBytes();

    @Before
    public void setup() {
        insertInto("byteTable").value("id", 1).value("code", CODE).execute();
        insertInto("byteTable").value("id", 2).execute(); // code == null
    }

    @Test
    public void insert() {
        assertThat(select("code").from("byteTable").where("id", 1).getBytes()).containsExactly(CODE);
        assertThat(select("code").from("byteTable").where("id", 2).getBytes()).isNull();
    }

    @Test
    public void upsert() {
        insertOrUpdateInto("byteTable").key("id", 3).value("code", CODE).execute();
        assertThat (select("code").from("byteTable").where("id", 1).getBytes())
                .containsExactly(CODE);
    }

    @Test(expected = AnswerNotUnique.class)
    public void selectTooMany() {
        select("code").from("byteTable").getBytes();
    }

    @Test
    public void update() {
        byte[] newCode = "SOMETHING".getBytes();
        update("byteTable").set("code", newCode).where("id", 1).execute();
        assertThat(select("code").from("byteTable").where("id", 1).getBytes()).containsExactly(newCode);
    }

    @Test
    public void select() {
        assertThat(
                select("id").from("byteTable").where("code", CODE).getInt()
        ).isEqualTo(1);
    }

    @Test
    public void select2() {
        assertThat(
                select("id").from("byteTable where code=?").parameter(CODE).getInt()
        ).isEqualTo(1);
    }

    @Test
    public void groupBy() {
        assertThat(
                select("sum(id)").from("byteTable")
                        .groupBy("code").having("code", CODE).getInt()
        ).isEqualTo(1);
    }

    @Test
    public void parameterList() {
        assertThat(
                select("id").from("byteTable").where(
                        new NamedParameterList().with("code", CODE)
                ).getInt()
        ).isEqualTo(1);
    }


}
