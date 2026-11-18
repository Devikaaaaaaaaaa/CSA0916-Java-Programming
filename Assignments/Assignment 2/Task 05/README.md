# Task 05

**Assignment:** Java Programming – Assignment 2 (CO2)

## Compile and run

```bash
javac src/GenericLeaderboard.java
java -cp src GenericLeaderboard
```

## Coverage

Generic Leaderboard<T> uses Comparator<? super T>, bounded wildcard input List<? extends T>, sorted copy and stable deterministic tie-breaks. Demonstrates faulty subtraction comparator risk.

Run on the Java version installed in your lab. Benchmark timings vary by JVM, warm-up, hardware and system load; execute multiple times and retain raw measurements for your report.
