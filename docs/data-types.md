Data types
==========

(Back to the [README](../Readme.md))

* [Supported types](#supported-types)
* [Arrays](#arrays)
* [Enumerated types](#enumerated-types)
* [Utility methods](#utility-methods)

Supported types
---

The `value`, `where` and `set` methods (and also `parameter` and `key`, see [Statements](statements.md))
support the following data types: `boolean`, `int`, `double` (SQL type: DOUBLE PRECISION),
`String`, `byte[]` (BYTEA),
`LocalDate` (DATE), `LocalTime` (TIME), 
`LocalDateTime` (TIMESTAMP) and `Instant` (TIMESTAMP WITH TIMEZONE).

See [Queries](queries.md) for the methods that retrieve values of these types.

Arrays
---

The following array types are also supported (not for `key`): `String[]`, `Integer[]`, `int[]`,
`Double[]` and `double[]`, and their 2-dimensional
counterparts `String[][]`, `Integer[][]`, `int[][]`, `Double[][]` and `double[][]`.
(Internally arrays of `int` and `double` are converted to and from arrays of `Integer` and `Double`.)
Other array types can be used with the more general methods that take an `Object[]` and the name of the
SQL element type, for example

    Long[] someLongs = ...;
    insertInto("myTable").value("longs", someLongs, "BIGINT").execute();

or by implementing the `Parameter` interface (see source).

Array parameters can also be used in where clauses, for example

    int[] ids = {1,2,3};
    select("name").from("persons")
        .where("id = ANY(?)", ids) 
        .getList(rs -> rs.getString("name"));

Enumerated types
---

Also enumerated types are supported, provided the elements of the PostgreSQL enumerated type
are the same as the names of the Java enum constants, also in upper case. For example, in Postgres:

    CREATE TYPE Color AS ENUM ('BLUE', 'RED', 'GREEN');

in Java:

    enum Color {
        BLUE, RED, GREEN;
    }

The name of an enum constant is sent to the database without a type. PostgreSQL infers the type from the
context, for example from the column it is compared with or assigned to. Hence the names of the Java and
PostgreSQL types do not need to correspond, and enum parameters can also be used with question marks
in your own SQL

    select("id").from("colors").where("color IN (?, ?)", Color.RED).parameter(Color.BLUE)...

When the context does not determine the type, the value is treated as text. If necessary, add a cast
yourself, for example `?::color`.

Enum parameters may be `null`, but then a cast is needed to select the correct method

    insertInto("colors").value("color", (Color) null).execute();

Utility methods
---
We provide some small enhancements to the JDBC API for working with the Java 8 time library. Every class that extends `BaseDAO`
has access to the following methods, equivalents of the methods `getInt(..)`, `getString(..)`, .. of the `ResultSet` interface:

    Instant instant = getInstant(resultSet, "columnName")
    Instant instant = getInstant(resultSet, columnNumber)
    LocalDate localDate = getLocalDate(resultSet, "columnName")
    LocalDate localDate = getLocalDate(resultSet, columnNumber)
    LocalTime localTime = getLocalTime(resultSet, "columnName")
    LocalTime localTime = getLocalTime(resultSet, columnNumber)
    LocalDateTime localDateTime = getLocalDateTime(resultSet, "columnName")
    LocalDateTime localDateTime = getLocalDateTime(resultSet, columnNumber)
