# Task A1 / B1 – Bus-pass file format and operations

## Delimited text record
`passId|registerNo|name|routeNo|boardingStop|validUntil(ISO-8601)|amountPaid`
Example: `P1003|STU1003|Kavya|12|Main Gate|2027-03-31|1250.0`
Escape or reject pipe/newline characters in text fields; validate exact field count, date and numeric ranges.

## Binary record (DataOutputStream)
Write fields in a fixed documented order: writeUTF(passId), writeUTF(registerNo), writeUTF(name), writeInt(routeNo), writeUTF(stop), writeUTF(validUntil), writeDouble(amount). Prefix each record with a length/version marker for robust future parsing. DataOutputStream alone does not make records self-describing.

## Pseudocode – add
```text
ALGORITHM AddPass(pass)
  VALIDATE all required fields and ranges
  OPEN file in append mode
  WRITE complete encoded record
  FLUSH and CLOSE file
  RETURN success
END ALGORITHM
```
## Pseudocode – find
```text
ALGORITHM FindPass(id)
  IF file does not exist THEN RETURN NOT_FOUND
  OPEN buffered reader
  FOR EACH line
    TRY parse and validate exact field count
      IF record.id equals id THEN RETURN record
    CATCH malformed record: log line number and continue
  END FOR
  RETURN NOT_FOUND
END ALGORITHM
```
## Pseudocode – list route
```text
ALGORITHM ListRoute(route)
  result ← empty list
  FOR EACH valid record in file
    IF record.route equals route THEN append record
  RETURN result
END ALGORITHM
```
Missing file: report clearly or treat as empty only for read-only lookup; create parent directories for add. Damaged line: quarantine/log it with line number, skip it, and continue. Incomplete write/power failure: use temp-file rewrite plus atomic rename for updates, checksums/length framing for binary records, backups and fsync where durability is required. A trailing partial record should fail validation and be ignored/quarantined.

For 8,000 records, sequential text scanning is simple and inspectable. Fixed-length RandomAccessFile records allow direct offset `(index × recordSize)`, approximately O(1) access after locating the index, but updates and schema changes are less flexible and text fields need fixed-width encoding. The supplied PassStore uses a variable-length delimited file and sequential O(n) lookup.
