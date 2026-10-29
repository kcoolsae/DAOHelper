DAOHelper library
=========
(This project is distributed under the MIT License - see [LICENSE](LICENSE) and [AUTHORS](AUTHORS))

This Java library provides a 'fluent' abstraction of the most common JDBC calls 
and base classes to ease the use of the **data access object** design pattern.

**Important note** The library should only be used on a PostgreSQL database, 
although most of its methods would probably work on any SQL database.

Example
---

A data access object extends `BaseDAO` and uses its fluent interface to access the database.
There is no need for `try catch` blocks or for setting parameters by hand

    class JdbcPersonDao extends BaseDAO implements PersonDao {

        JdbcPersonDao(BaseDAC context) {
            super(context);
        }

        public int addPerson(String name, String firstName) {
            return insertInto("persons")
                .value("name", name)
                .value("first_name", firstName)
                .create();
        }

        public Optional<String> findName(int id) {
            return select("name").from("persons")
                .where("id", id)
                .findString();
        }

        public List<Person> listPersons() {
            return select("id, name, first_name").from("persons")
                .orderBy("name")
                .getList(rs -> new Person(rs.getInt("id"), rs.getString("name"), rs.getString("first_name")));
        }
    }

Documentation
---

* [The DAO pattern](docs/dao-pattern.md): DAOs, data access contexts, transactions and exceptions
* [Statements](docs/statements.md): insert, update, delete, upsert, where clauses, stored procedures
* [Queries](docs/queries.md): select, getters and finders, sorting and paging, grouping, joins, master/detail
* [Data types](docs/data-types.md): supported types, arrays, enumerated types
* [How to build this library](docs/build.md)
* [Changes in each version](CHANGELOG.md)
