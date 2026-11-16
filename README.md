# COMP3005 Assignment 3 - Question 1

Nicholas Geldrez, 101334230

A Java console app that connects to a PostgreSQL database with JDBC and does CRUD operations on a `students` table.

**Video demo:** https://youtu.be/isWwHmBZGfg

## Files

```
A3_Q1/
├── database/
│   └── students.sql      creates the students table and inserts the 3 starting rows
├── src/
│   └── StudentApp.java   the application (all 4 functions + a menu)
├── lib/
│   └── (put the PostgreSQL JDBC driver .jar here)
└── README.md
```

## What you need

- PostgreSQL and pgAdmin 4
- Java JDK (any recent version, I used JDK 26)
- PostgreSQL JDBC driver, from https://jdbc.postgresql.org/download/ (download the `.jar` and put it in the `lib` folder)

## Setting up the database

1. Open pgAdmin and connect to your PostgreSQL server.
2. Right click **Databases** > **Create** > **Database...**, name it `students_db` and save.
3. Click on `students_db`, open **Tools** > **Query Tool**.
4. Open `database/students.sql` (or paste it in) and run it.
5. The last line selects everything from the table, so you should see John Doe, Jane Smith and Jim Beam.

The script drops the table first, so you can run it again any time to reset the table back to the original 3 students.

## Connection settings

These are at the top of `src/StudentApp.java`:

```java
static String url = "jdbc:postgresql://localhost:5432/students_db";
static String user = "postgres";
static String password = "mypass123";
```

Change these to match your own database before compiling.

### Note on my setup

I used the PostgreSQL server on my Carleton VM instead of a local install. Port 5432 isn't open on the VM, so I connected through an SSH tunnel, which forwards `localhost:5432` on my laptop to the database on the VM. Because of that the code still uses `localhost` and nothing had to change. To do the same, leave this running in its own terminal while using the app:

```bash
ssh -L 5432:localhost:5432 <vm-username>@<vm-address>
```

If PostgreSQL is installed on your own computer you don't need this, just run the app normally.

## Compiling and running

Run these from the `A3_Q1` folder.

Compile:

```bash
javac -d out src/StudentApp.java
```

Run (macOS / Linux):

```bash
java -cp "out:lib/*" StudentApp
```

Run (Windows):

```bat
java -cp "out;lib/*" StudentApp
```

If it connects you'll see `Connected to PostgreSQL successfully!` and then the menu:

```
1. Get all students
2. Add a student
3. Update a student's email
4. Delete a student
0. Exit
```

## Functions

- **`getAllStudents()`** - runs `SELECT` on the students table and prints every row ordered by id.
- **`addStudent(first_name, last_name, email, enrollment_date)`** - inserts a new student. The id is made by the database (`SERIAL`). The date has to be `YYYY-MM-DD`.
- **`updateStudentEmail(student_id, new_email)`** - updates the email for that id. Prints a message if no student has that id.
- **`deleteStudent(student_id)`** - deletes the student with that id. Prints a message if no student has that id.

The queries that take input use `PreparedStatement` so the values are passed separately from the SQL. If something goes wrong (like adding an email that already exists, which breaks the `UNIQUE` constraint) the error message is printed and the program keeps running.

To see the changes from add/update/delete, run `SELECT * FROM students;` in pgAdmin's Query Tool, or right click the table > **View/Edit Data** > **All Rows**.
