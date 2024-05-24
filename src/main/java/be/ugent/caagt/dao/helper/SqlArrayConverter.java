/*
 * SqlArrayConverter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.Array;
import java.sql.SQLException;

/**
 * Converts an sql array to a Java array of some type
 */
@FunctionalInterface
public interface SqlArrayConverter<T> {

    T[] convert (Array array) throws SQLException;

    SqlArrayConverter<String> TO_STRING_ARRAY = a -> (String[])a.getArray();

    SqlArrayConverter<Integer> TO_INTEGER_ARRAY = a -> (Integer[])a.getArray();

    SqlArrayConverter<Double> TO_DOUBLE_ARRAY = a -> (Double[])a.getArray();

}
