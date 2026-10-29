Statements
==========

(Back to the [README](../Readme.md))

* [Fluent interface](#fluent-interface)
* [Inserting new records](#inserting-new-records)
* [Delete / update](#delete--update)
* [Where](#where)
* [Upsert](#upsert)
* [Grouping where clauses](#grouping-where-clauses)
* [Stored procedures](#stored-procedures)
* [Low level calls](#low-level-calls)

See also: [Data types](data-types.md), [Queries](queries.md)

Fluent interface
---
The main purpose of this library is to provide a **fluent interface** for making SQL calls. This interface
is available to every class that extends `BaseDAO`. Its main purpose is to hide exception handling 
(there is no need to put the method calls in `try catch` blocks) and
streamline parameter handling. Although some SQL syntax errors can be avoided
by using this library, occasionally you will still need to write some explicit SQL.

The examples provided below should serve as a good
starting place

Inserting new records
---
 
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
               
Delete / update
---

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
                
Where
---

Where clauses default to equality, but can also be more complicated. For example: delete
everything with `value` exceeding some limit:

    deleteFrom("someTable")
        .where ("value >= ?", someLimit).execute();
        
A where clause can also be without parameter

    update("someTable")
        .set ("value", 0)
        .where ("value IS NULL").execute();

Upsert
---

We also support '_upserts_': operations where a new record is added to the database, or replaced if it already exists.
Here we distinguish between _keys_, which are used to determine whether the record already exists, and _values_ that 
are either inserted into a new record or replace the values in an existing record.

    insertOrUpdateInto("myTable")
        .key("id", someId)
        .value("name", someString)
        .value("number", someInt)
        .execute();
        
Upserts without values are allowed. Upserts without keys behave like normal inserts.        

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
        return compositeWhereClause()
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

Stored procedures
---

To call a stored procedure, use `call`:

    call("insertPersonWithAddress(?,?,?,?,?,?)")
        .parameter(someName).parameter(someFirstName)
        .parameter(someLine1).parameter(someLine2)
        .parameter(someZip).parameter(someTown)
        .execute();
        
Parameters are not named and must be provided in the correct order.
       
If the stored procedure returns a value, use `select` (this is the 
standard convention for PostgreSQL).
       
    int posNum = select("GREATEST(0,?)").parameter(someNum).noFrom().getInt();
     
The `noFrom` indicates that the `select` is not related to a specific table. (In future versions
we want to provide a `call` syntax for this type of queries.)

Low level calls
---

There is a general `sql` method which allows you to execute statements that cannot be formulated with any of the other
methods. Accepts the `parameter` method.

At an even lower level, there are statements `prepareStatement`, `prepareCall` and `prepareAutoGenerated`
which simply delegate to the underlying connection.
