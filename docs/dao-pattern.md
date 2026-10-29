The DAO pattern
===============

(Back to the [README](../Readme.md))

* [DAOs, DACs and DAPs](#daos-dacs-and-daps)
* [Transactions](#transactions)
* [Exceptions](#exceptions)

DAOs, DACs and DAPs
---

The library supports the following three level **data access object** pattern

* To access the database, the client makes method calls that reside in one or more
_data access objects_ (DAOs). DAOs are defined as public interfaces with hidden implementations. 
DAO implementations must extend `BaseDAO` to use this library
* DAOs can not be created directly but must be retrieved from a _data access context_ (DAC). A data access context 
is an abstraction of a database connection: methods executed on DAOs obtained from the same DAC will use
the same database connection. DACs also provide transaction support. DACs are defined as public interfaces with hidden implementations. 
DAC implementations must extend `BaseDAC`
* DACs must be retrieved from a _data access provider_ (DAP). A DAP is an abstraction of a database. Clients are responsible
for implementing the DAP, there is no direct support from this library.

Every class that extends `BaseDAO` has access to the methods described in
[Statements](statements.md) and [Queries](queries.md).

Transactions
---

Transactions are started, committed and rolled back through the DAC

    try (DataAccessContext dac = provider.getContext()) {
        dac.begin();
        try {
            dac.getPersonDao().deletePerson(someId);
            dac.getAddressDao().deleteAddressesOf(someId);
            dac.commit();
        } catch (RuntimeException ex) {
            dac.rollback();
            throw ex;
        }
    }

(Here `DataAccessContext`, `getPersonDao` and `getAddressDao` are your own classes and methods.)
Without a call to `begin` every statement is committed immediately. Closing a DAC
restores this auto-commit behaviour. Note that this also commits a transaction that is still open,
hence the explicit `rollback` in the example.

Exceptions
---
All checked exceptions thrown by the underlying JDBC library are wrapped into unchecked
exceptions of type `DataAccessException`.

Some violations of database constraints result in a more specific subclass of `DataAccessException`:
`UniqueViolation`, `ForeignKeyViolation` and `NotNullViolation`.
Queries throw `NotFound` when they expect a result and there is none, and `AnswerNotUnique`
when they expect at most one row and there are more.
Errors in the transaction methods of a DAC (`begin`, `commit`, `rollback` and `close`)
result in a `DACException`.
