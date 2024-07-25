DAOHelper library
=========
(This project is distributed under the MIT License - see [LICENSE](LICENSE) and [AUTHORS](AUTHORS))

See also
* [How to build this library](docs/build.md)

This Java library provides a 'fluent' abstraction of the most common JDBC calls 
and base classes to ease the use of the **data access object** design pattern.

**Important note** The library should only be used on a PostgreSQL database, 
although most of its methods would probably work on any SQL database.

**Also note** Features from 1.1.6 onwards are not yet fully documented.

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

* [Grouping where clauses](#grouping-where-clauses)

## Contents

* [DAO pattern](#dao-pattern)
* [Utility method](#utility-methods)
* [Fluent interface](#fluent-interface)
* [Master/detail](#masterdetail)
* [Specials](#specials)
* [Grouping where clauses](#grouping-where-clauses)
* [Exceptions](#exceptions)

DAO pattern
----

The library supports the following three level **data access object** pattern

* To access the database, the client makes method calls that reside in one or more
_data access objects_ (DAOs). DAOs are defined as public interfaces with hidden implementations. 
DAO implementations must extend `BaseDAO` to use this library
* DAOs can not be created directly but must be retrieved from a _data access context_ (DAC). A data access context 
is an abstraction of a database connection: methods executed on DAOs obtained from the same DAC will use
the same database connection. DACs also provide transaction support. DACs are defined as public interfaces with hidden implementations. 
DAC implementations must extend `BaseDAC`
* DACs must be retreived from a _data access provider_ (DAP). A DAP is an abstraction of a database. Clients are responsible
for implementing the DAP, there is no direct support from this library.

Utility methods
---
We provide some small enhancements to the JDBC API for working with the new Java 8 time library. Every class that extends `BaseDAO`
has access to the following methods, equivalents of the methods `getInt(..)`, `getString(..)`, .. of the `ResultSet` interface:

    Instant instant = getInstant(resultSet, "columnName')
    Instant instant = getInstant(resultSet, columnNumber)
    LocalDate localDate = getLocalDate(resultSet, "columnName')
    LocalDate localDate = getLocalDate(resultSet, columnNumber)
    LocalTime localTime = getLocalTime(resultSet, "columnName')
    LocalTime localTime = getLocalTime(resultSet, columnNumber)
    LocalDateTime localDateTime = getLocalDateTime(resultSet, "columnName')
    LocalDateTime localDateTime = getLocalDateTime(resultSet, columnNumber)
       
See also: [Data types](#data-types)

Fluent interface
---
The main purpose of this library is to provided a **fluent interface** for making SQL calls. This interface
is available to every class that extends `BaseDAO`. Its main purpose is to hide exception handling 
(there is no need to put the method calls in `try catch` blocks) and
streamline parameter handling. Although some SQL syntax errors can be avoided
by using this library, occasionally you will still need to write some explicit SQL.

The examples provided below should serve as a good
starting place

### Inserting new records
 
Insert a new record into a table
 
 
    insertInto("myTable")
        .value("name", someString)
        .value("number", someInt)
        .execute();

Insert a new record and retrieve the auto-generated key (must be an `int`)

    int key = insertInto("myOtherTable")
        .value("comment", someString)
        .value("flag", someBoolean)
        .create();
               
### Delete / update      

Delete all records satisfying a certain condition

    deleteFrom("myTable")
        .where("name", someName)
        .where("number", someNumber)
        .execute();
        
Update some fields in all records satisfying a certain condition

    update("myOtherTable")
        .set("comment", newComment)
        .set("flag", true)
        .where("id", someId).execute();
        
An update clause can be without parameter

    update("myTable")
        .set("count = count + 1")
        .where("category", someCategory).execute();
        
or can contain question marks in more complicated cases

    update("myTable")
        .set("count = count + ?", someIncrement)
        .where("category", someCategory).execute();
                
### Where

Where clauses default to equality, but can also be more complicated. For example: delete
everything with `value` exceeding some limit:

    deleteFrom("someTable")
        .where ("value >= ?", someLimit).execute();
        
A where clause can also be without parameter

    update("someTable")
        .set ("value", 0)
        .where ("value IS NULL").execute();

### Data types

The `value`, `where` and `set` methods (and also `parameter` and `key` - see later)
support the following data types: `boolean`, `int`, 
`String`,
`LocalDate` (SQL type: DATE), `LocalTime` (TIME), 
`LocalDateTime` (TIMESTAMP) and `Instant` (TIMESTAMP WITH TIMEZONE).

Some array types are also supported, in particular `String[]`, `Integer[]` and `int[]`. 
(Internally the latter is converted to and from `Integer[]`.) Other array types can currently not be used directly
but workarounds exist by calling more general methods and/or subclassing `Parameter` (see source).

Also enumerated types are supported, provided you follow the following conventions:
* The name of the Java enumerated type and the PostgresQL enumerated type must be the same
* The PostgreSQL enumerated type elements must be the same as the element names, and also in upper case.
* For example, in Postgres:

      CREATE TYPE Color AS ENUM ('BLUE', 'RED', 'GREEN');
  in Java:

      enum Color {
           BLUE, RED, GREEN;
      }
           
### Select

The structure of a select statement is very similar to the examples given above, except that
instead of `execute` we provide a variety of methods to retrieve the results.

The simplest select statements are those that expect a single result

    int count = select("count(*)").from("myTable")
        .where("number >= ?", someNumber)
        .getOneInt();
        
The methode `getOneInt` throws an exception when the select returns no results or
more than a single result. A variant `getInt` returns 0 when there is no result and throws an exception when there
is more than one result.

     int userId = select("id").from("users")
         .where("name", someName).getInt();
         
(For ease of use, the database should be set up in such a way that 
record identifiers of value 0 are avoided where possible.) 

Similar `get` and`getOne` methods exist for `String` and the four supported
 date/time data types.
The `get` methods return `null` when there is no result. There is also
a `getOneBoolean`.

For multicolumn queries you can use `getObject` or `getOneObject`, but then
you must provide a `ResultSetConverter` which translates a result set row into 
an object of your choice (usually a lambda or a method reference).

    Person person = select("name, firstName").from("persons")
                      .where("id",userId)
                      .getObject( rs ->
                           new Person(rs.getString("name"), rs.getString("firstName"))
                      );
                      
    Address address = select ("line1, line2, zip, town, country").from("addresses")
                          .where("id", addressId)
                          .getObject(AddressDao::makeAddress);
                          
For queries that potentially return more than one result, you use `getList`                          
        
    List<Address> list = select ("line1, line2, zip, town, country").from("addresses")
                           .where("town LIKE '%' || ? || '%'", searchString)
                           .getList(AddressDao::makeAddress);
        
There is also a method `isEmpty` which checks whether a query returns 0 rows
        
    boolean noDebt = select ("1").from("invoices").where("not paid").isEmpty();        

### Sorting and paging

After the `where` clauses you can add one or more `orderBy` clauses to define the order
in which the results are listed

    List<Person> persons = select ("id, name, firstName, promotionDate").from("persons")
                            .where ("promotionDate <= ?", todaysDate)
                            .orderBy ("promotionDate", false)
                            .orderBy ("name", true)
                            .getList(PersonDao::makePerson);
                            
The second argument to `orderBy` indicates whether the order is ascending (true) or descending (false). 
If true, this argument can be omitted.
                            
Instead of returning the full list, a query can also be asked to be return only a single _page_
of the result.
                            
    List<Person> persons = select ("id, name, firstName, promotionDate").from("persons")
                            .orderBy ("name"). orderBy ("firstName)
                            .onlyPage(0, 30)
                            .getList(PersonDao::makePerson);
                                
The first argument to `onlyPage` is the page number (starting at 0), the second argument
is the page size. You can combine `onlyPage` with `getPage` which instead of a list returns
a special `Page` object

    Page<Person> page = select ("id, name, firstName, promotionDate").from("persons")
                            .orderBy ("name"). orderBy ("firstName")
                            .onlyPage(0, 30)
                            .getPage(PersonDao::makePerson);
                            
The `Page` object not only contains the list of results (retrieve with `page.getList()`) but also
the _full size_ of the query result (retrieve with `page.getFullSize()`), i.e., the 
size the query result would have if there had been no `onlyPage` in the call. (This
should be faster than asking for the full size in a separate query.)

### Group by ... having

We can also group results 

    List<LocalDate> dates = select ("promotionDate").from("persons")
                .groupBy("promotionDate")
                .getList(rs -> getLocalDate(rs,"promotionDate");

and restrict the groups with `having`

    List<Integer> matches = select ("userId").from("addresses")
                .groupBy("userId")
                .having("count(addresses.id) > 1")
                .getList(rs -> rs.getInt(1));
                
As in SQL applications of `where` must come before those of `having`                                      

### Joins

There is no special `join` method. Joins must be written in SQL syntax, as part of the `from` method
that follows the select.

    List<PersonWithAddress> list =
          select ("name, firstName, line1, line2, zip, town")
              .from ("users JOIN addresses ON addresses.userId = users.id")
              .getList(PersonDao::makePersonWithAddress); 
        
### Upsert

We also support '_upserts_': operations where a new record is added to the database, or replaced if it already exists.
Here we distinguish between _keys_, which are used to determine whether the record already exists, and _values_ that 
are either inserted into a new record or replace the values in an existing record.

    insertOrUpdateInto("myTable")
        .key("id", someId)
        .value("name", someString)
        .value("number", someInt)
        .execute();
        
Upserts without values are allowed. Upserts without keys behave like normal inserts.        

## Master/detail

Consider a set of persons which each can have 0 or more addresses. In the database we represent this
as a (master) table `persons`, say with a primary key `id`, and a (detail) table `addresses` which refers to `persons` 
through a foreign key `userId`. 

Suppose we want to retrieve all persons satisfying a certain condition together
with all their addresses. We want to represent the result as a collection of `Person` objects where each
such objects contains a list of `Address` objects. And we want to do this with a minimum of calls to
the database:

* A first query to obtain the information stored in the `persons` table for the selected persons
* A second query will provide us with _all_ addresses for the selected persons

How do we join these results into a single list?

* Store the results of the first query into a map where the primary key of `persons` serves as
the key for the map
* Use this map while running over the second query to store the addresses with the correct person
* (Optional) store the map values into a list or set

The library provides some utility methods to do just this. To create the map, you write

    Map<Integer,Person> map 
        = select ("id, name, firstName").from("persons")
            .where(...).orderBy("id")
            .getMap(PersonDao::makePerson);

This assumes that the first column retrieved is the primary key and that it is an integer. (There
also exists a more general version of `getMap` without these restrictions). We also suppose that
the `Person` object created by `makePerson` already contains an empty list of addresses.
 
The second query now reads as follows

    select("users.id, addresses.id, line1, line2, town, zip")
        .from ("persons JOIN addresses ON users.id = addresses.userId")
        .where(...).orderBy("users.id")
        .processMap(map, (person,rs) -> person.addAddress(AddressDao.makeAddress(rs)); 

The resulting `map` now contains the combined information of both queries.                       .

Again this assumes that the first column contains the integer primary key of the master table. (And 
again there exists a version of `processMap` without these restrictions.)

## Specials

### Stored procedures

To call a stored procedure, use `call`:

    call("insertPersonWithAddress(?,?,?,?,?,?)").
        .parameter(someName).parameter(someFirstName)
        .parameter(someLine1).parameter(someLine2)
        .parameter(someZip).parameter(someTown)
        .execute();
        
Parameters are not named and must be provided in the correct order.
       
If the stored procedure returns a value, use `select` (this is the 
standard convention for PostgreSQL).
       
     int posNum = select("GREATEST(0,?)").parameter(someNum).noFrom().getInt()
     
The `noFrom` indicates that the `select` is not related to a specific table. (In future versions
we want to provide a `call` syntax for this type of queries.)
     
### Select with parameters

The `parameter` method can also be used after the `from` of a select call. This can be useful
with subselects or for certain types of join:

    List<Person> list = select("users.id, addresses.id")
        .from ("persons LEFT JOIN addresses ON users.id = addresses.userId AND zip = ?")
        .parameter(requiredZip)
        .orderBy("users.id").getList(...);
        
(Returns a list with all persons with an address or NULL depending on whether there is 
an address for that person with a given zip code or not.)        

### Low level calls

There is a general `sql` method which allows you to execute statements that cannot be formulated with any of the other
methods. Accepts the `parameter` method.

At an even lower level, there are statements `prepareStatement`, `prepareCall` and `prepareAutoGenerated`
which simply delegate to the underlying connection.

Grouping where clauses
---
When you have a table with a composite primary key which in Java is represented as a class, you find yourself repeating
the same combination of where-clauses again and again:

    .where ("language", locale.getLanguage())
    .where ("country", locale.getCountry())
    .where ("variant", locale.getVariant()) 
Using a `CompositeWhereClause` may make this easier for you.
In this example you would first create a method `hasLocale` as follows

    protected CompositeWhereClause hasLocale (Locale locale) {
        return new CompositeWhereClause()
            .where ("language", locale.getLanguage())
            .where ("country", locale.getCountry())
            .where ("variant", locale.getVariant());
    }
which then allows you to simplify the triple call to `where` in a query to the following single call

    .where(hasLocale(locale))
Instead of a `CompositeWhereClause` you can also use a `NamedParameterList`:

    protected NamedParameterList hasLocale (Locale locale) {
        return new NamedParameterList()
            .with ("language", locale.getLanguage())
            .with ("country", locale.getCountry())
            .with ("variant", locale.getVariant());
    }
which only supports simple queries of the form name=parameter, which is sufficient in most cases. The advantage of
these named parameter lists is that they can also be used as arguments to methods `key`, `value` and `set`.   
    
Exceptions
---
All checked exceptions thrown by the underlying JDBC library are wrapped into unchecked
exceptions of type `DataAccessException`.
