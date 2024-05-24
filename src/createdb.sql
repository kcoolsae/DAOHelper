-- createdb.sql
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
--
-- This software is distributed under the MIT License - see files LICENSE and AUTHORS
-- in the top level project directory.

-- creates the database tables for testing

CREATE TABLE persons (
  id             SERIAL PRIMARY KEY,
  name           TEXT,
  first_name     TEXT,
  number         INTEGER,
  flag           BOOLEAN,
  registered     TIMESTAMP,
  birthday       DATE,
  starts_work_at TIME,
  stamp          TIMESTAMP WITH TIME ZONE
);

CREATE TABLE details (
  id INTEGER,
  nr INTEGER
);

CREATE TABLE stuff (
  id INTEGER PRIMARY KEY,
  val1 TEXT,
  val2 INTEGER
);

CREATE TABLE things (
  id1  INTEGER,
  id2  TEXT,
  val1 TEXT,
  val2 INTEGER,
  PRIMARY KEY (id1, id2)
);

CREATE TABLE question (
  moduleId   INTEGER,
  questionId SERIAL
);

CREATE TABLE arrayTable (
  id INTEGER,
  strs TEXT[],
  ints INTEGER[],
  doubles DOUBLE PRECISION[]
);

CREATE TABLE coordinates (
    x DOUBLE PRECISION PRIMARY KEY,
    y DOUBLE PRECISION,
    Z DOUBLE PRECISION
);

CREATE TABLE byteTable (
    id INTEGER PRIMARY KEY,
    code BYTEA
);

CREATE OR REPLACE FUNCTION insert_person(n TEXT, f TEXT)
  RETURNS VOID AS $$
BEGIN
  INSERT INTO persons (name, first_name) VALUES (n, f);
END;
$$ LANGUAGE 'plpgsql';

CREATE TYPE Color AS ENUM ('RED', 'GREEN', 'BLUE');

CREATE TABLE colors (
    id INTEGER PRIMARY KEY,
    color Color
);

CREATE TABLE colorNames (
    color Color PRIMARY KEY,
    name TEXT
);
