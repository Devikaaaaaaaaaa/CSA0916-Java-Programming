# Task 5 – Static members and object life cycle

See Task 5 in ../REPORT.md. ParkingTicket uses a static monotonically increasing ticket counter. Active ticket objects are referenced by the active-ticket map and become eligible for garbage collection after exit removes the final application reference.