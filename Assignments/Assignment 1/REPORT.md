# Assignment 1 – Campus Smart-Parking System
## Programming in Java | CO1 | Tasks 1–12

**Student name:** To be completed by student  
**Register number:** To be completed by student  
**Java:** JDK 17 or later

> **Important assumptions:** The question paper does not prescribe exact prices, slot proportions, or the precise expiry semantics of a one-day pass. The implementation uses clearly labelled illustrative rates. Confirm them with faculty, run the code locally, and add genuine screenshots before submission.

## Scenario
The system manages multiple floors and numbered slots; two-wheelers, cars and EVs; EV charging; unique tickets and entry times; slot release and exit billing. Valid monthly passes for students/staff waive fees; visitors pay. Task 12 adds e-bikes (flat fee), delivery vans (double car rate during 09:00–17:00), visitor one-day passes and five unequal-capacity floors.

## Task 1 – Understand the problem and identify objects (4 marks)
Candidate classes:
- Vehicle: registration and category; keeps identity and category together.
- ParkingSlot: floor, number, slot kind and occupant; enforces single occupancy.
- ParkingFacility: owns floors and allocates/releases slots.
- ParkingTicket: unique ID, vehicle, user/pass details, slot and entry timestamp.
- ParkingUser/UserType: distinguishes student, staff and visitor eligibility.
- FeePolicy: centralises rates, time windows and pass rules.
- ParkingSystem: coordinates entry, ticket lookup, exit and occupancy.

Business rules: one vehicle per slot; one active ticket per registration; ticket IDs are never reused during the process; only compatible slots may be allocated; release must reference a valid occupied slot; exit cannot precede entry; eligible monthly/one-day passes waive the fee; charging is billed separately; invalid input must not corrupt occupancy.

Assumptions: registration is uppercased; one active visit per registration; each started hour is billed as a full hour; illustrative rates are two-wheeler ₹10/hour, car ₹30/hour, EV parking ₹40/hour plus ₹12/kWh, e-bike ₹25/visit. Van billing uses the car base plus one additional car-rate for each started hour overlapping 09:00–17:00. These rates and interpretations are examples, not supplied by the question paper. The sample slot mix is configured deterministically and should be replaced by actual campus inventory.

Alternative: objects group attributes and behaviour. Linear search by registration is O(n); adding a field means changing the class and construction/access methods. Parallel arrays regNo[], type[], entryTime[] also need O(n) search, but every insert/delete/sort must keep all arrays aligned and a new attribute requires another parallel array. Both require O(n) storage; objects use extra reference/object overhead but are safer and easier to extend.

## Task 2 – Choose data representation (3 marks)
| Data | Java type | Reason |
|---|---|---|
| Floor/slot index | int | Whole-number coordinates |
| Registration | String | Alphanumeric identifier |
| Entry/exit | LocalDateTime | Date-time arithmetic |
| Duration | long minutes | Handles long stays |
| Fee | double in demo; BigDecimal in production | Monetary calculations |
| Categories | enum | Restricts valid values |
| Active tickets | HashMap<String, ParkingTicket> | Average O(1) lookup by registration |

A jagged array ParkingSlot[][] represents unequal floor capacities. A rectangular array wastes cells or cannot represent different row lengths. Flattening requires prefix offsets; floor × slotsPerFloor + slot is valid only when every floor has equal capacity. ArrayList per floor is flexible but has collection overhead.

Integer division risk: 130 / 60 evaluates to 2, not 2.166…. Casting afterwards does not repair it: (double)(130 / 60) is still 2.0. The ceiling number of started hours is 3. Use (minutes + 59) / 60. Coordinate access is O(1); scanning all N slots is O(N); jagged storage is O(N).

## Task 3 – Control logic (4 marks)
Allocation pseudocode:
```text
ALLOCATE(vehicleNo)
  reject blank vehicleNo
  FOR each floor f
    FOR each slot s on floor f
      IF slot[f][s] is empty THEN
        slot[f][s] <- vehicleNo
        RETURN (f,s)
  RETURN NO_SLOT
```
Release pseudocode:
```text
RELEASE(f,s)
  IF f is outside floor range RETURN false
  IF s is outside that floor's slot range RETURN false
  IF slot[f][s] is empty RETURN false
  slot[f][s] <- empty
  RETURN true
```
Fee pseudocode:
```text
FEE(ticket, exit, energy)
  duration <- minutes between entry and exit; reject if negative
  IF eligible monthly pass or one-day pass RETURN 0
  IF e-bike RETURN flat visit fee
  hours <- ceiling(duration / 60)
  fee <- hours * vehicle rate
  IF EV add energy * charging rate
  IF van add daytime overlap surcharge for 09:00–17:00
  RETURN fee
```
The completed ParkingFacility.java is in src/campus/parking. Its expected output is:
```text
[0, 0]
[0, 1]
[1, 0]
null
true false false
[0, 1]
```
Linear scan allocation/release is O(N) worst case and O(N) storage. A stack of free-slot indexes can give amortised O(1) allocation/release but needs O(N) additional index storage and must avoid duplicate entries.

## Task 4 – Classes, objects and constructors
A class is a blueprint; an object is an instance. Vehicle, ParkingSlot and ParkingTicket are separate concepts. Constructors initialise required state and validate it so an invalid ticket is not temporarily exposed. Overloaded constructors provide alternative valid creation forms; this(...) chains constructors and super(...) initialises inherited state. A default constructor followed by setters permits incomplete intermediate states. Static factories can validate and name creation choices. Creating n objects is O(n) total time and O(n) retained space for all approaches; safety and maintainability are the meaningful differences.

## Task 5 – Static members and object life cycle (3 marks)
A ticket counter belongs to the class, not an individual ticket; otherwise each ticket would have its own counter and IDs could repeat. Static methods suit operations independent of an instance. On exit, the active-ticket map removes the ticket and the slot is cleared. If no live reference remains, the ticket becomes eligible for garbage collection; collection time is not guaranteed. Java passes object references by value: a copied reference can access the same object. A method can return a ticket reference to its caller. A global counter needs O(1) state and O(1) increment work per ticket.

## Task 6 – Inheritance hierarchy (4 marks)
```text
                 Vehicle (abstract)
                 /             \\
          TwoWheeler            Car
                                  |
                             ElectricCar
                             implements Chargeable
```
Vehicle holds registration and common fee behaviour. TwoWheeler and Car supply their rates; ElectricCar extends Car and implements the optional Chargeable capability. This demonstrates hierarchical and multilevel inheritance. Constructor trace: Vehicle constructor → Car constructor → ElectricCar constructor. super(regNo) initialises the parent first. Composition alternative: Vehicle has a VehicleType/rate-policy object. Composition allows changing pricing independently and is more flexible when rules evolve; inheritance is straightforward for a small stable type hierarchy.

## Task 7 – Method overriding (4 marks)
Subclasses override ratePerHour(). @Override lets the compiler verify the override. An override must have a compatible signature and return type and cannot reduce visibility. final is appropriate for immutable registration fields, classes that must not be subclassed, and methods that enforce invariants. equals(), hashCode() and toString() are overridden in the demo. Equality by registration means Car("TN02B2") equals ElectricCar("TN02B2"); this is acceptable only if registration alone defines identity. If category is part of identity, revise equals/hashCode consistently or enforce uniqueness in the service. A switch-based fee method and virtual dispatch are both O(1) for a fixed set of types; polymorphism's main benefit is reduced coupling and easier extension, not asymptotic speed.

## Task 8 – Polymorphism (4 marks)
Compile-time polymorphism is method/constructor overloading. Runtime polymorphism occurs when a Vehicle reference refers to a TwoWheeler, Car or ElectricCar and the JVM invokes the concrete overridden method. Dispatch trace: declared type Vehicle; actual object ElectricCar; call calculateFee(130); shared method computes 3 billable hours; overridden rate method supplies the rate; Chargeable adds energy cost. A chain of k instanceof checks may perform O(k) checks; virtual dispatch is O(1) selection. FeeDemo.java completes the supplied exercise. Expected output:
```text
TwoWheeler TN01A1 -> Rs 30.0
Car TN02B2 -> Rs 90.0
ElectricCar TN03C3 -> Rs 210.0
true
```

## Task 9 – Abstract classes and interfaces
Vehicle is abstract because it contains shared state, a constructor and common fee logic. Chargeable is an interface because charging is an optional capability. A class may implement multiple interfaces. Default interface methods provide shared optional behaviour; static interface methods provide related utilities. If Vehicle became an interface, it could not provide ordinary per-instance fields and constructor initialization like an abstract class. If Chargeable became an abstract class, a class already extending Car could not also extend it. Runtime dispatch remains O(1) asymptotically.

## Task 10 – Packages and encapsulation (3 marks)
All Java files use package campus.parking. Private fields prevent arbitrary state changes; public methods expose supported operations; package-private helpers remain within the package; protected members support subclasses. Validate registration, duration, slot coordinates and energy close to the state they protect. Wrapper types can represent absence but null unboxing can fail. Compare wrapper values with equals(), not ==, because == checks object identity. Public fields would require every caller to repeat validation, increasing defect risk; central validation is O(1) per update.

## Task 11 – Implement, test and defend (5 marks)
ParkingSystem.java integrates entry, slot compatibility, ticket creation, fee calculation, release and pass rules. Test matrix:
| ID | Scenario | Expected result |
|---|---|---|
| 1 | Car enters/exits after 125 minutes | 3 started hours; slot released |
| 2 | E-bike visit | Flat ₹25 |
| 3 | Van 10:00–12:00 | Car base plus daytime surcharge |
| 4 | Visitor with valid one-day pass | ₹0 |
| 5 | Student/staff with valid monthly pass | ₹0 |
| 6 | Duplicate active registration | Entry rejected |
| 7 | Unknown registration exits | Exit rejected |
| 8 | No compatible slot | Entry rejected, no ticket |
| 9 | Zero-minute stay | 0 billable hours |
| 10 | Exactly 60 minutes | 1 billable hour |
| 11 | Last compatible free slot | Allocated |
| 12 | Invalid floor/slot release | false; occupancy unchanged |

Run each case locally and record actual results before claiming it passed. Array lookup for active tickets is O(n); ArrayList lookup is O(n); HashMap keyed by registration is average O(1), with O(n) storage. The integrated implementation uses HashMap for frequent exit lookup.

## Task 12 – Re-evaluate and adapt (8 marks)
**A. Impact:** Unequal floor capacities require a jagged array, with each floor row created at its own length. The new vehicle categories affect category representation, slot compatibility and pricing. Visitor one-day pass validity needs an explicit expiry definition in production.

**B. Evaluation:** An abstract Vehicle hierarchy supports new subtypes without editing existing vehicle subclasses. However, time-window surcharges and pass eligibility are pricing policies, not simply vehicle identity; spreading these rules across subclasses makes them hard to audit. Centralise fee policy. The facility representation must change if it assumed a rectangular array.

**C. Decision – MODIFY:** Retain Vehicle, ParkingSlot, ParkingTicket, encapsulation and the service boundary. Modify facility storage to jagged arrays, extend the category/capability model, and centralise pricing. Add e-bike, delivery-van and one-day-pass rules. Trade-offs: central policy is auditable but can grow; enums suit a fixed category set but a dynamic registry is needed for plug-in categories; HashMap improves lookup but uses more memory; one-day pass needs a defined expiry timestamp.

New-category validation targets: e-bike visit → flat ₹25; van 10:00–12:00 → base plus daytime surcharge; van 18:00–20:00 → base only; valid visitor one-day pass → ₹0; capacities {4,5,3,6,4} → five unequal floor rows. The sample implementation includes these paths; run and record each test yourself.

## Conclusion
The design applies Java fundamentals and OOP to the campus parking scenario. The recommended solution modifies the original design with jagged floor storage, ticket lookup by registration and centralised fee policy. Fill in personal details, confirm illustrative tariff assumptions with faculty, run all programs and attach genuine execution screenshots before submission.