# Task A3 / B3 – DAO and JDBC batch persistence

The repeated JDBC workflow is: obtain connection, prepare parameterized SQL, bind values, execute, map ResultSet rows to objects, handle SQL exceptions, close resources. SQL text, table/column names and row-to-object mapping vary by entity.

Generic DAO contract: insert, findById, findAll, update, delete. Student mapping: Student.studentId → student.student_id (primary key); registerNo → register_no; fullName → full_name; email → email. StudentDao demonstrates parameterized CRUD. A generic DAO reduces repeated service/GUI persistence code and centralizes data access, but very generic abstractions can hide useful SQL-specific operations. Direct SQL in UI/service tightly couples layers and makes tests/migrations harder.

Batching reduces round trips: add rows to PreparedStatement batch, executeBatch every 1,000, and commit one transaction. On any failure rollback. For 100,000 rows, benchmark Statement, PreparedStatement, batches and auto-commit vs one transaction. Do not invent timings: record measured elapsed times on the target machine. DatabaseMetaData and ResultSetMetaData reveal product/driver/table and column information.
