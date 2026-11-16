-- COMP3005 Assignment 3 Q1
-- Creates the students table and adds the starting data.
-- Run this in pgAdmin's Query Tool while connected to the students_db database.
-- It drops the table first so it can be re-run to reset everything back to the original 3 rows.

DROP TABLE IF EXISTS students;

CREATE TABLE students (
    student_id      SERIAL  PRIMARY KEY,     -- auto-increment id
    first_name      TEXT    NOT NULL,
    last_name       TEXT    NOT NULL,
    email           TEXT    NOT NULL UNIQUE,
    enrollment_date DATE
);

-- initial data from the assignment
INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES
('John', 'Doe', 'john.doe@example.com', '2023-09-01'),
('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),
('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');

-- check that it worked
SELECT * FROM students;
