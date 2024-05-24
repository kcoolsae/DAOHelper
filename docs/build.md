Building this library
===
Instructions below are for the Linux operating system. The library can possibly also be built
on MacOS and/or Windows (bash), but we do not provide instructions for this.

Prerequisites
-------
To build this library you need
* Java version 17 or newer
* SBT build system, version 1.9.1 or newer

To run the tests (and to use this library at run time), you need
* PostgreSQL database server, version 15 or newer

Running the unit tests
-------

To run the unit tests, a local PostgreSQL database server must be running and have a
database with the name `daohelpertestdb`. To create the test database, open `psql` with administrator privileges
and use the following commands

    CREATE USER daohelpertestuser ENCRYPTED PASSWORD 'daohelper';
    CREATE DATABASE daohelpertestdb OWNER daohelpertestuser ENCODING 'UTF8';
    \q

After that, create the necessary tables using the `creatdb.sql` script
located in the `src` directory

    $ cd ..../src
    $ psql -h localhost -U daohelpertestuser daohelpertestdb < createdb.sql

and add the following line the file `.pgpass` in your home directory

    localhost:5432:daohelpertestdb:daohelpertestuser:daohelper

To run the unit tests, execute the following command from the top level project directory

    $ sbt clean test

## Building the library

The following command, executed from the top level project directory, installs the library into the local ivy repository

    $ sbt clean publishLocal

Alternatively, using the following command from the top level project directory

    $ sbt clean package

will create the file `daohelper-1.1.12.jar` in the `target` directory.