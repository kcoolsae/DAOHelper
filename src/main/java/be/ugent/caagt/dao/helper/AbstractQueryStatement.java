/*
 * AbstractQueryStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.AnswerNotUnique;
import be.ugent.caagt.dao.DataAccessException;
import be.ugent.caagt.dao.NotFound;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Common super class for query statements
 */
abstract class AbstractQueryStatement extends AbstractFilteredStatement {

    /**
     * Creates an uninitialized object of this type. Should be initialized with {@link #copyAux}.
     */
    protected AbstractQueryStatement() {
        //
    }

    protected boolean isCompoundQuery() {
        return false;
    }

    protected  AbstractQueryStatement(String stat, BaseDAC context) {
        super(stat, context);
    }

    /**
     * Return the query result as a single int, or 0 when no result is returned.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public int getInt() throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int result = rs.getInt(1);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single integer result");
                    } else {
                        return result;
                    }
                } else {
                    return 0;
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Return the query result as an optional int.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public OptionalInt findInt() throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int result = rs.getInt(1);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single integer result");
                    } else {
                        return OptionalInt.of(result);
                    }
                } else {
                    return OptionalInt.empty();
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Return the query result as a single double, or NaN when no result is returned.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public double getDouble() throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double result = rs.getDouble(1);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single double precision result");
                    } else {
                        return result;
                    }
                } else {
                    return Double.NaN;
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Return the query result as an optional double
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public OptionalDouble findDouble() throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double result = rs.getDouble(1);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single double precision result");
                    } else {
                        return OptionalDouble.of(result);
                    }
                } else {
                    return OptionalDouble.empty();
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Return the query result as a single boolean, or false if no result was returned.
     */
    public boolean getBoolean() throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean result = rs.getBoolean(1);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single boolean result");
                    } else {
                        return result;
                    }
                } else {
                    return false;
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Return the query result as a single boolean.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     * @throws NotFound        when the query result was empty
     */
    public boolean getOneBoolean() throws DataAccessException {
        return getOneObject(ResultSetConverter.FIRST_BOOLEAN);
    }

    /**
     * Return the query result as an optional boolean.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public Optional<Boolean> findBoolean() throws DataAccessException {
        return findObject(ResultSetConverter.FIRST_BOOLEAN);
    }

    /**
     * Return the query result as a single int.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     * @throws NotFound        when the query result was empty
     */
    public int getOneInt() throws DataAccessException {
        return getOneObject(ResultSetConverter.FIRST_INTEGER);
    }

    /**
     * Return the query result as a single double.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     * @throws NotFound        when the query result was empty
     */
    public double getOneDouble() throws DataAccessException {
        return getOneObject(ResultSetConverter.FIRST_DOUBLE);
    }

    /**
     * Return the query result as a single string, or null when no result is returned.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public String getString() throws DataAccessException {
        return getObject(ResultSetConverter.FIRST_STRING);
    }

    /**
     * Return the query result as a single string, or return
     * the given defaultValue when no result is returned.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public String getStringOrDefault(String defaultValue) throws DataAccessException {
        String result = getObject(ResultSetConverter.FIRST_STRING);
        return result == null ? defaultValue : result;
    }

    /**
     * Return the query result as a single string, or an empty string when no result is returned.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public String getStringOrEmpty() throws DataAccessException {
        return getStringOrDefault("");
    }

    /**
     * Return the query result as a single String.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     * @throws NotFound        when the query result was empty
     */
    public String getOneString() throws DataAccessException {
        return getOneObject(ResultSetConverter.FIRST_STRING);
    }

    /**
     * Return the query result as a single String.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public Optional<String> findString() throws DataAccessException {
        return findObject(ResultSetConverter.FIRST_STRING);
    }

    /**
     * Return the query result as a single object, or null when no result is returned.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public <U> U getObject(ResultSetConverter<U> converter) throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    U result = converter.convert(rs);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single row as a result");
                    }
                    return result;
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Retrieves the query result and call the provided method to convert the
     * corresponding result set to an object of type U. Clients should only use this
     * method if none of the 'get'-methods in this class suits their needs.
     */
    public <U> U convert(ResultSetConverter<U> converter) {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return converter.convert(rs);
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Return the query result as a single object.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     * @throws NotFound        when the query result was empty
     */
    public <U> U getOneObject(ResultSetConverter<U> converter) throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    U result = converter.convert(rs);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single row as result");
                    } else {
                        return result;
                    }
                } else {
                    throw new NotFound("Query returned empty result");
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Return the query result as an optional object.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public <U> Optional<U> findObject(ResultSetConverter<U> converter) throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    U result = converter.convert(rs);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single row as result");
                    } else {
                        return Optional.of(result);
                    }
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }


    /**
     * Return a list of objects as the result of a query.
     */
    public <U> List<U> getList(ResultSetConverter<U> rsc) throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                List<U> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rsc.convert(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }

    }

    public LocalDateTime getLocalDateTime() {
        return getObject(ResultSetConverter.FIRST_LOCAL_DATE_TIME);
    }

    public Optional<LocalDateTime> findLocalDateTime() {
        return findObject(ResultSetConverter.FIRST_LOCAL_DATE_TIME);
    }

    public LocalDateTime getOneLocalDateTime() {
        return getOneObject(ResultSetConverter.FIRST_LOCAL_DATE_TIME);
    }

    public LocalDate getLocalDate() {
        return getObject(ResultSetConverter.FIRST_LOCAL_DATE);
    }

    public Optional<LocalDate> findLocalDate() {
        return findObject(ResultSetConverter.FIRST_LOCAL_DATE);
    }

    public LocalDate getOneLocalDate() {
        return getOneObject(ResultSetConverter.FIRST_LOCAL_DATE);
    }

    public LocalTime getLocalTime() {
        return getObject(ResultSetConverter.FIRST_LOCAL_TIME);
    }

    public Optional<LocalTime> findLocalTime() {
        return findObject(ResultSetConverter.FIRST_LOCAL_TIME);
    }

    public LocalTime getOneLocalTime() {
        return getOneObject(ResultSetConverter.FIRST_LOCAL_TIME);
    }

    public Instant getInstant() {
        return getObject(ResultSetConverter.FIRST_INSTANT);
    }

    public Optional<Instant> findInstant() {
        return findObject(ResultSetConverter.FIRST_INSTANT);
    }

    public Instant getOneInstant() {
        return getOneObject(ResultSetConverter.FIRST_INSTANT);
    }

    /**
     * Returns the current result set as a map. The iteration order on the map is the insertion order,
     * so it remains useful to invoke this getter with ordered queries.
     */
    public <K, V> Map<K, V> getMap(ResultSetConverter<K> keyConverter, ResultSetConverter<V> valueConverter) {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                Map<K, V> result = new LinkedHashMap<>();
                while (rs.next()) {
                    K key = keyConverter.convert(rs);
                    V value = valueConverter.convert(rs);
                    result.put(key, value);
                }
                return result;
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    public <V> Map<Integer, V> getMap(ResultSetConverter<V> valueConverter) {
        return getMap(ResultSetConverter.FIRST_INTEGER, valueConverter);
    }

    /**
     * Uses the result of the current query to further process values of a map generated by {@link #getMap}.
     * For every row in the result, the given value processor is applied to the element in the map whose
     * key is determined by the key converter.
     */
    public <K, V> void processMap(Map<K, V> map, ResultSetConverter<K> keyConverter, ResultSetProcessor<V> valueProcessor) {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    K key = keyConverter.convert(rs);
                    valueProcessor.process(map.get(key), rs);
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    public <V> void processMap(Map<Integer, V> map, ResultSetProcessor<V> valueProcessor) {
        processMap(map, ResultSetConverter.FIRST_INTEGER, valueProcessor);
    }


    /**
     * Processes the result of the current query without returning a result. The given value processor
     * is applied to the destination parameter.
     */
    public <V> void process(V destination, ResultSetProcessor<V> valueProcessor) {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    valueProcessor.process(destination, rs);
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    /**
     * Processes the result of the current query without returning a result. The given
     * consumer is called for every row in the result set.
     */
    public void process(ResultSetConsumer consumer) {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    consumer.accept(rs);
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    public <U> U[] getSqlArray(SqlArrayConverter<U> converter) {
        return getObject(rs -> {
            Array array = rs.getArray(1);
            return array == null ? null : converter.convert(array);
        });
    }

    /**
     * Return an array of bytes that corresponds to BYTEA-type in the database.
     *
     * @throws AnswerNotUnique when the query resulted in more than one row
     */
    public byte[] getBytes() throws DataAccessException {
        try (PreparedStatement ps = prepareStatement()) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] result = rs.getBytes(1);
                    if (rs.next()) {
                        throw new AnswerNotUnique("Expected a single row as a result");
                    }
                    return result;
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }


    public String[] getArrayOfString() {
        return getSqlArray(SqlArrayConverter.TO_STRING_ARRAY);
    }

    public int[] getArrayOfInt() {
        return BoxUtils.unboxArray(getArrayOfInteger());
    }

    public Integer[] getArrayOfInteger() {
        return getSqlArray(SqlArrayConverter.TO_INTEGER_ARRAY);
    }

    public double[] getArrayOfDouble() {
        return BoxUtils.unboxArray(getArrayOfWrappedDouble());
    }

    public Double[] getArrayOfWrappedDouble() {
        return getSqlArray(SqlArrayConverter.TO_DOUBLE_ARRAY);
    }

    public Integer[][] get2DimArrayOfInteger() {
        return getSqlArray(a -> (Integer[][]) a.getArray());
    }

    public String[][] get2DimArrayOfString() {
        return getSqlArray(a -> (String[][]) a.getArray());
    }

    public int[][] get2DimArrayOfInt() {
        return BoxUtils.unboxArray(get2DimArrayOfInteger());
    }

    public Double[][] get2DimArrayOfWrappedDouble() {
        return getSqlArray(a -> (Double[][]) a.getArray());
    }

    public double[][] get2DimArrayOfDouble() {
        return BoxUtils.unboxArray(get2DimArrayOfWrappedDouble());
    }

    public static class EnumRSConverter<E extends Enum<E>> implements ResultSetConverter<E> {
        private final Class<E> enumClass;

        public EnumRSConverter(Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public E convert(ResultSet rs) throws SQLException {
            return Enum.valueOf(enumClass, rs.getString(1));
        }
    }

    public <E extends Enum<E>> E getEnum (Class<E> enumClass) {
        return getObject(new EnumRSConverter<>(enumClass));
    }

    public <E extends Enum<E>> E getOneEnum (Class<E> enumClass) {
        return getOneObject(new EnumRSConverter<>(enumClass));
    }

    public <E extends Enum<E>> Optional<E> findEnum (Class<E> enumClass) {
        return findObject(new EnumRSConverter<>(enumClass));
    }
}
