/*
 * ResultSetConverter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Converts one row of a result set to an object of some type
 */
@FunctionalInterface
public interface ResultSetConverter<T> {
    T convert(ResultSet rs) throws SQLException;

    ResultSetConverter<Boolean> FIRST_BOOLEAN = rs -> rs.getBoolean(1);
    ResultSetConverter<Integer> FIRST_INTEGER = rs -> rs.getInt(1);
    ResultSetConverter<Double> FIRST_DOUBLE = rs -> rs.getDouble(1);
    ResultSetConverter<String>  FIRST_STRING = rs -> rs.getString(1);

    ResultSetConverter<LocalDateTime> FIRST_LOCAL_DATE_TIME = rs -> BaseDAO.getLocalDateTime(rs, 1);
    ResultSetConverter<Instant> FIRST_INSTANT = rs -> BaseDAO.getInstant(rs, 1);
    ResultSetConverter<LocalDate> FIRST_LOCAL_DATE = rs -> BaseDAO.getLocalDate(rs, 1);
    ResultSetConverter<LocalTime> FIRST_LOCAL_TIME = rs -> BaseDAO.getLocalTime(rs, 1);

}
