# Task A2 / B2 – Buffered I/O and adversarial input

A 50 MiB file contains 52,428,800 bytes. Calling read() once per byte makes approximately 52.4 million Java-level read calls. Without buffering, many calls can cross the Java/native/OS boundary; BufferedInputStream fills a 64 KiB block and serves individual bytes from memory, requiring roughly 800 block refills plus loop iterations. Actual OS reads may be further combined by the OS cache.

Buffering gives little benefit when the source already supplies large blocks (e.g. a byte-array or already-buffered stream), or when processing is dominated by CPU work rather than I/O. Files.readAllLines() is convenient but stores all decoded lines: O(n) memory in file size and O(n) time. Streaming buffered reading uses O(buffer size) auxiliary memory and O(n) time.

Adversarial test cases for the pass file: wrong field count, non-numeric amount, empty line, negative amount, and embedded delimiter. The parser must reject or safely skip each malformed row and continue to later records. For login testing, use a local disposable database only. Unsafe string concatenation can allow crafted text to change SQL syntax; a PreparedStatement treats supplied values as data, not executable SQL.
