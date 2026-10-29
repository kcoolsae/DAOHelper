Queries
=======

(Back to the [README](../Readme.md))

* [Select](#select)
* [Getters](#getters)
* [Finders](#finders)
* [Objects and lists](#objects-and-lists)
* [Sorting and paging](#sorting-and-paging)
* [Group by ... having](#group-by--having)
* [Joins](#joins)
* [Select with parameters](#select-with-parameters)
* [Intersection](#intersection)
* [Master/detail](#masterdetail)

See also: [Data types](data-types.md), [Statements](statements.md)

Select
---

The structure of a select statement is very similar to the [other statements](statements.md), except that
instead of `execute` we provide a variety of methods to retrieve the results.

The simplest select statements are those that expect a single result

    int count = select("count(*)").from("myTable")
        .where("number >= ?", someNumber)
        .getOneInt();
        
The method `getOneInt` throws an exception when the select returns no results or
more than a single result. A variant `getInt` returns 0 when there is no result and throws an exception when there
is more than one result.

    int userId = select("id").from("users")
        .where("name", someName).getInt();
         
(For ease of use, the database should be set up in such a way that 
record identifiers of value 0 are avoided where possible.) 

Getters
---

Similar `get` and `getOne` methods exist for `double`, `String`, the four supported
date/time data types and enumerated types.
The `get` methods return `null` when there is no result (`getDouble` returns `NaN`). There are also
`getBoolean` (returns `false` when there is no result) and `getOneBoolean`.
Enumerated types need the class as an argument

    Color color = select("color").from("colors").where("id", someId).getEnum(Color.class);

Strings have two more variants: `getStringOrEmpty` returns an empty string, and `getStringOrDefault(defaultValue)`
the given default value, when there is no result.

Byte arrays and arrays are retrieved with `getBytes` and with methods like `getArrayOfString`, `getArrayOfInt`,
`getArrayOfInteger`, `getArrayOfDouble`, `getArrayOfWrappedDouble`, and the corresponding `get2DimArrayOf...`.
These return `null` when there is no result.

    int[] numbers = select("ints").from("arrayTable").where("id", someId).getArrayOfInt();

Finders
---

For queries that may or may not return a result, the `find` methods return an `Optional`
(or `OptionalInt`, `OptionalDouble`) instead

    Optional<String> name = select("name").from("persons")
        .where("id", someId)
        .findString();

The result is empty when the query returns no row, but also when the selected value is `NULL`.
When you need to distinguish between these two cases, use `findObject` with a converter for the
entire row (see below). The result is then present whenever the row exists, even when some of its values are `NULL`

    Optional<Person> person = select("name, firstName").from("persons")
        .where("id", someId)
        .findObject(rs -> new Person(rs.getString("name"), rs.getString("firstName")));

Objects and lists
---

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

To handle each row of the result yourself, without building a result, use `process`

    select("name, firstName").from("persons")
        .process(rs -> System.out.println(rs.getString("name")));

Sorting and paging
---

After the `where` clauses you can add one or more `orderBy` clauses to define the order
in which the results are listed

    List<Person> persons = select ("id, name, firstName, promotionDate").from("persons")
                            .where ("promotionDate <= ?", todaysDate)
                            .orderBy ("promotionDate", false)
                            .orderBy ("name", true)
                            .getList(PersonDao::makePerson);
                            
The second argument to `orderBy` indicates whether the order is ascending (true) or descending (false). 
If true, this argument can be omitted.
                            
Instead of returning the full list, a query can also be asked to return only a single _page_
of the result.
                            
    List<Person> persons = select ("id, name, firstName, promotionDate").from("persons")
                            .orderBy ("name").orderBy ("firstName")
                            .onlyPage(0, 30)
                            .getList(PersonDao::makePerson);
                                
The first argument to `onlyPage` is the page number (starting at 0), the second argument
is the page size. You can combine `onlyPage` with `getPage` which instead of a list returns
a special `Page` object

    Page<Person> page = select ("id, name, firstName, promotionDate").from("persons")
                            .orderBy ("name").orderBy ("firstName")
                            .onlyPage(0, 30)
                            .getPage(PersonDao::makePerson);
                            
The `Page` object not only contains the list of results (retrieve with `page.getList()`) but also
the _full size_ of the query result (retrieve with `page.getFullSize()`), i.e., the 
size the query result would have if there had been no `onlyPage` in the call. (This
should be faster than asking for the full size in a separate query.)

Group by ... having
---

We can also group results 

    List<LocalDate> dates = select ("promotionDate").from("persons")
                .groupBy("promotionDate")
                .getList(rs -> getLocalDate(rs,"promotionDate"));

and restrict the groups with `having`

    List<Integer> matches = select ("userId").from("addresses")
                .groupBy("userId")
                .having("count(addresses.id) > 1")
                .getList(rs -> rs.getInt(1));
                
As in SQL applications of `where` must come before those of `having`                                      

Joins
---

There is no special `join` method. Joins must be written in SQL syntax, as part of the `from` method
that follows the select.

    List<PersonWithAddress> list =
          select ("name, firstName, line1, line2, zip, town")
              .from ("users JOIN addresses ON addresses.userId = users.id")
              .getList(PersonDao::makePersonWithAddress); 

Select with parameters
---

The `parameter` method can also be used after the `from` of a select call. This can be useful
with subselects or for certain types of join:

    List<Person> list = select("users.id, addresses.id")
        .from ("persons LEFT JOIN addresses ON users.id = addresses.userId AND zip = ?")
        .parameter(requiredZip)
        .orderBy("users.id").getList(...);
        
(Returns a list with all persons with an address or NULL depending on whether there is 
an address for that person with a given zip code or not.)        

Intersection
---

The method `intersection` combines two or more select statements with SQL `INTERSECT`. The result
can be sorted, paged and retrieved like an ordinary select

    List<Integer> ids = intersection(
            select("id").from("persons").where("flag", true),
            select("userId").from("addresses").where("town", someTown)
        ).orderBy("id")
        .getList(rs -> rs.getInt(1));

Master/detail
---

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
        .processMap(map, (person,rs) -> person.addAddress(AddressDao.makeAddress(rs))); 

The resulting `map` now contains the combined information of both queries.

Again this assumes that the first column contains the integer primary key of the master table. (And 
again there exists a version of `processMap` without these restrictions.)
