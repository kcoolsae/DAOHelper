/*
 * FGTestBase.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import org.junit.Before;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public abstract class FGTestBase extends TestDAO {

    protected final static LocalDateTime REGISTRATION_TIME
            = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    protected final static LocalDate BIRTHDAY = LocalDate.of(1980, 5, 6);
    protected final static LocalTime STARTS_WORK_AT = LocalTime.of(8,30);
    protected final static Instant STAMP = Instant.ofEpochSecond(100000);

    @Before
    public void setup() {
        insertInto("persons")
                .value("name", "Bond")
                .value("first_name", "James")
                .value("number", 7)
                .value("flag", true)
                .value("registered", REGISTRATION_TIME)
                .value("birthday", BIRTHDAY)
                .value("starts_work_at", STARTS_WORK_AT)
                .value("stamp", STAMP)
                .execute();
        insertInto("persons")
                .value("name", "Doe")
                .value("first_name", "John")
                .value("number", 6)
                .value("flag", false)
                .value("registered", REGISTRATION_TIME)
                .value("birthday", BIRTHDAY)
                .value("starts_work_at", STARTS_WORK_AT)
                .value("stamp", STAMP)
                .execute();
    }

}
