/*
 * SupportsWhere.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Interface that introduces default convenience methods for all classes in this package that support the 'where'-method
 */
public interface SupportsWhere<T> {

    /**
     * Adds a where clause to the current statement
     */
    T where(WhereClause clause);

    default T where(String clause) {
        return where(new WhereClause(clause));
    }

    default T where(String clause, boolean parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, int parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, double parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, String parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, LocalDateTime parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, LocalDate parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, LocalTime parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, Instant parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, byte[] parameter) {
        return where(new WhereClause(clause, parameter));
    }

    default T where(String clause, String[] parameter) {
        return where(clause, parameter, "TEXT");
    }

    default T where(String clause, String[][] parameter) {
        return where(clause, parameter, "TEXT");
    }

    default T where(String clause, Integer[] parameter) {
        return where(clause, parameter, "INTEGER");
    }

    default T where(String clause, Integer[][] parameter) {
        return where(clause, parameter, "INTEGER");
    }

    default T where(String clause, Double[] parameter) {
        return where(clause, parameter, "float8");
    }

    default T where(String clause, Double[][] parameter) {
        return where(clause, parameter, "float8");
    }

    default T where(String clause, int[] parameter) {
        return where(new WhereClause(clause, new IntArrayParameter(parameter)));
    }

    default T where(String clause, int[][] parameter) {
        return where(new WhereClause(clause, new Int2DimArrayParameter(parameter)));
    }

    default T where(String clause, double[] parameter) {
        return where(new WhereClause(clause, new DoubleArrayParameter(parameter)));
    }

    default T where(String clause, double[][] parameter) {
        return where(new WhereClause(clause, new Double2DimArrayParameter(parameter)));
    }

    default T where(String clause, Object[] parameter, String typeName) {
        return where(new WhereClause(clause, new SqlArrayParameter(parameter, typeName)));
    }

    <E extends Enum<E>> T where(String clause, E parameter);
}
