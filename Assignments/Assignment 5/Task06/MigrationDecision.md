# Task B4 – Migration and final engineering decision

## Migration workflow
1. Read the delimited pass file with a buffered reader.
2. Validate exact field count, ID, register number, route, ISO date and non-negative amount.
3. Record rejected line number, raw line and validation reason in a separate error report.
4. Batch valid rows using PreparedStatement.addBatch(), executing every 1,000 records.
5. Disable auto-commit and commit only after all batches succeed; rollback on failure.
6. Use DatabaseMetaData to report database product, driver name/version and available tables.
7. Verify inserted row count and compare source, accepted and rejected counts.

## Scale decision
For 12 campuses and 100× current volume, a relational database is the preferred system of record. It supports concurrent counter operations, indexed search, constraints, transactions, backups, authorization and centralized reporting. Text files remain useful for import/export and audit snapshots, but concurrent updates and consistency require additional locking and recovery design. Java serialization is unsuitable as the primary multi-user store because it is Java-specific, opaque to SQL analytics and difficult to coordinate across processes.

One shared database offers unified identity, reporting and transactional consistency, but needs tenant/campus keys, access controls, capacity planning and availability. Separate serialized files per campus isolate local data but complicate cross-campus search, synchronization, backup, schema evolution and concurrent access. Use campus_id in relational tables and indexes suited to access patterns.

## Measurements to report
Record file size and pass count; buffered/unbuffered line count times; scan-vs-HashMap lookup time; JDBC Statement/PreparedStatement/batch insert times at 1k/10k/100k; auto-commit vs transaction; memory/heap; and rejected-row count. Do not substitute guessed measurements for execution results.
