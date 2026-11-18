# Task 8 – Polymorphism and completed code

Source: ../src/campus/parking/FeeDemo.java

Compile and run:
```bash
javac -d out src/campus/parking/*.java
java -cp out campus.parking.FeeDemo
```
The list is declared as List<Vehicle> while holding different concrete types. calculateFee is dispatched at runtime. The overload/constructor examples and dispatch trace are in ../REPORT.md.