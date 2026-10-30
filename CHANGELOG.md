Changes
=======

(Back to the [README](Readme.md))

#### New in version 1.1.14
* **Changed behaviour:** the `find` methods (`findInt`, `findString`, `findObject`, …) now return an empty
  result both when the query returns no row and when the value is `NULL`.
  Previously `findInt`, `findDouble` and `findBoolean` returned 0 or false for a `NULL` value,
  and the other finders threw a `NullPointerException`.
  See [Finders](docs/queries.md#finders).
* **Changed behaviour:** the number of parameters is no longer checked in advance, which saves
  a round trip to the database for every statement. A `?` that has no type from its context (for example
  `select("?").parameter(17).noFrom()`) now gets the type of the Java value instead of `text`.
  Too many parameters still cause an `IllegalArgumentException`, but too few parameters now cause a
  `DataAccessException` when the statement is executed.
* `set` now supports `String[][]`
* Updates can take values from other tables with `from` (PostgreSQL `UPDATE … FROM`).
  See [Delete / update](docs/statements.md#delete--update).
* An update without `where` can now be executed (and then applies to all records)
* `key` (upserts), `having` and `with` (named parameter lists) now support arrays, and `having` and `with`
  also support enumerated types. `key` also accepts a `Parameter` object, like `value`.
* Named parameter lists should now be created with `namedParameterList()` in the DAO or DAC, so that
  enumerated types are translated as configured. The constructor `new NamedParameterList()` is deprecated.
* Documentation brought up to date with the features added since version 1.1.6, and split
  into several files

#### New in version 1.1.13.1
* Bug fix: enumerated type parameters can now be `null`, for example

      insertInto("colors").value("color", (Color) null).execute();
* Bug fix: `getEnum` and `getOneEnum` now return `null` when the database value is `NULL`
* Bug fix: enum constants with a body are now translated to the correct database type

#### New in version 1.1.13
* Where clauses can use array parameters, for example

      int[] ids = {1,2,3};
      select("name").from("persons")
          .where("id = ANY(?)", ids) 
          .getList(rs -> rs.getString("name"));

#### New in version 1.1.12
* Translation of enumerated types can be configured
* `parameter` methods now support arrays

#### New in version 1.1.11
* `getContext` in `BaseDAO` now protected
* `Parameter` interface now public
* Some support for enumerated types

#### New in version 1.1.10.1
* Resolved paging bug with intersection
* Improved paging by using window functions to obtain total size instead
  of using a separate query
* Used materialized with to speed up window function

(Releases 1.1.9 and 1.1.10 were buggy…)

#### New in version 1.1.8
* Timestamps truncated to microseconds
* Extended support for byte arrays 

#### New in version 1.1.7
* Added finders, i.e. getters that return an Optional
* Added (minimal) support for 2-dim arrays, arrays of doubles,
  byte arrays

#### New in version 1.1.6
* Support for doubles

#### New in version 1.1.5

* [Grouping where clauses](docs/statements.md#grouping-where-clauses)
