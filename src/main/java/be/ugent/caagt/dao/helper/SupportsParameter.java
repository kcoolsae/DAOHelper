/*
 * SupportsParameter.java
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
 * Interface that introduces default convenience methods for all classes in this package that support the 'parameter'-method
 */
public interface SupportsParameter<T extends AbstractSQLStatement> {

    T parameter(Parameter parameter);

    default T parameter(String p) {
        return parameter(new StringParameter(p));
    }

    default T parameter(int p) {
        return parameter(new IntParameter(p));
    }

    default T parameter(double p) {
        return parameter(new DoubleParameter(p));
    }

    default T parameter(boolean p) {
        return parameter(new BooleanParameter(p));
    }

    default T parameter(LocalDateTime p) {
        return parameter(new LocalDateTimeParameter(p));
    }

    default T parameter(LocalDate p) {
        return parameter(new LocalDateParameter(p));
    }

    default T parameter(LocalTime p) {
        return parameter(new LocalTimeParameter(p));
    }

    default T parameter(Instant p) {
        return parameter(new InstantParameter(p));
    }

    default T parameter(byte[] p) {
        return parameter(new BytesParameter(p));
    }

    default T parameter(String[] value) {
        return parameter(value, "TEXT");
    }

    default T parameter(String[][] value) {
        return parameter(value, "TEXT");
    }

    default T parameter(Integer[] value) {
        return parameter(value, "INTEGER");
    }

    default T parameter(Integer[][] value) {
        return parameter(value, "INTEGER");
    }

    default T parameter(Double[] value) {
        return parameter(value, "float8");
    }

    default T parameter(Double[][] value) {
        return parameter(value, "float8");
    }

    default T parameter(int[] value) {
        return parameter(new IntArrayParameter(value));
    }

    default T parameter(int[][] value) {
        return parameter(new Int2DimArrayParameter(value));
    }

    default T parameter(double[] value) {
        return parameter(new DoubleArrayParameter(value));
    }

    default T parameter(double[][] value) {
        return parameter(new Double2DimArrayParameter(value));
    }

    default T parameter(Object[] value, String typeName) {
        return parameter(new SqlArrayParameter(value, typeName));
    }

    <E extends Enum<E>> T parameter (E value);

}
