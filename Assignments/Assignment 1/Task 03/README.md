# Task 3 – Control logic and completed code

Source: ../src/campus/parking/ParkingFacility.java

Compile and run:
```bash
javac -d out src/campus/parking/*.java
java -cp out campus.parking.ParkingFacility
```
The program scans floor-by-floor and slot-by-slot, returns the first free coordinate, and safely rejects invalid or already-free releases. Pseudocode and complexity discussion are in ../REPORT.md.