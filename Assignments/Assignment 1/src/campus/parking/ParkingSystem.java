package campus.parking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Integrated campus parking model. The rate assumptions are illustrative and are
 * stated in REPORT.md because the assignment sheet does not prescribe prices.
 */
public class ParkingSystem {
    enum VehicleType { TWO_WHEELER, CAR, ELECTRIC_CAR, E_BIKE, DELIVERY_VAN }
    enum UserType { STUDENT, STAFF, VISITOR }
    enum SlotType { STANDARD, CHARGING, LARGE }

    static final class Vehicle {
        private final String registration;
        private final VehicleType type;
        Vehicle(String registration, VehicleType type) {
            if (registration == null || registration.isBlank()) throw new IllegalArgumentException("Registration required");
            this.registration = registration.trim().toUpperCase(Locale.ROOT);
            this.type = Objects.requireNonNull(type);
        }
        String registration() { return registration; }
        VehicleType type() { return type; }
        @Override public boolean equals(Object o) { return o instanceof Vehicle v && registration.equals(v.registration); }
        @Override public int hashCode() { return registration.hashCode(); }
    }

    static final class ParkingSlot {
        final int floor, number;
        final SlotType type;
        String occupiedBy;
        ParkingSlot(int floor, int number, SlotType type) { this.floor=floor; this.number=number; this.type=type; }
        boolean isFree() { return occupiedBy == null; }
    }

    static final class ParkingTicket {
        private static long nextId = 1000;
        final String ticketId;
        final Vehicle vehicle;
        final UserType userType;
        final boolean monthlyPass, oneDayPass;
        final LocalDateTime entryTime;
        final ParkingSlot slot;
        ParkingTicket(Vehicle vehicle, UserType userType, boolean monthlyPass, boolean oneDayPass,
                      LocalDateTime entryTime, ParkingSlot slot) {
            this.ticketId = "PK-" + (++nextId);
            this.vehicle = vehicle; this.userType = userType; this.monthlyPass = monthlyPass;
            this.oneDayPass = oneDayPass; this.entryTime = entryTime; this.slot = slot;
        }
    }

    private final ParkingSlot[][] floors;
    private final Map<String, ParkingTicket> activeTickets = new LinkedHashMap<>();

    ParkingSystem(int[] slotsPerFloor) {
        if (slotsPerFloor == null || slotsPerFloor.length == 0) throw new IllegalArgumentException("Floors required");
        floors = new ParkingSlot[slotsPerFloor.length][];
        for (int f=0; f<slotsPerFloor.length; f++) {
            if (slotsPerFloor[f] < 1) throw new IllegalArgumentException("Each floor needs at least one slot");
            floors[f] = new ParkingSlot[slotsPerFloor[f]];
            for (int s=0; s<slotsPerFloor[f]; s++) {
                SlotType type = s < Math.max(1, slotsPerFloor[f] / 10) ? SlotType.CHARGING
                              : (s >= slotsPerFloor[f] - Math.max(1, slotsPerFloor[f] / 10) ? SlotType.LARGE : SlotType.STANDARD);
                floors[f][s] = new ParkingSlot(f, s+1, type);
            }
        }
    }

    ParkingTicket enter(Vehicle vehicle, UserType user, boolean monthlyPass, boolean oneDayPass, LocalDateTime now) {
        Objects.requireNonNull(vehicle); Objects.requireNonNull(user); Objects.requireNonNull(now);
        if (activeTickets.containsKey(vehicle.registration())) throw new IllegalArgumentException("Vehicle is already parked");
        if (monthlyPass && user == UserType.VISITOR) throw new IllegalArgumentException("Monthly pass is for students/staff");
        if (oneDayPass && user != UserType.VISITOR) throw new IllegalArgumentException("One-day pass is for visitors");
        ParkingSlot slot = findSlot(vehicle.type());
        if (slot == null) throw new IllegalStateException("No suitable slot available");
        slot.occupiedBy = vehicle.registration();
        ParkingTicket ticket = new ParkingTicket(vehicle, user, monthlyPass, oneDayPass, now, slot);
        activeTickets.put(vehicle.registration(), ticket);
        return ticket;
    }

    private ParkingSlot findSlot(VehicleType type) {
        SlotType required = switch (type) {
            case ELECTRIC_CAR, E_BIKE -> SlotType.CHARGING;
            case DELIVERY_VAN -> SlotType.LARGE;
            default -> SlotType.STANDARD;
        };
        for (ParkingSlot[] floor : floors)
            for (ParkingSlot slot : floor)
                if (slot.isFree() && slot.type == required) return slot;
        return null;
    }

    double exit(String registration, LocalDateTime now, int kWh) {
        ParkingTicket t = activeTickets.get(registration.toUpperCase(Locale.ROOT));
        if (t == null) throw new IllegalArgumentException("No active ticket for registration");
        if (now.isBefore(t.entryTime)) throw new IllegalArgumentException("Exit precedes entry");
        long minutes = Duration.between(t.entryTime, now).toMinutes();
        double fee = calculateFee(t, minutes, kWh);
        t.slot.occupiedBy = null;
        activeTickets.remove(t.vehicle.registration());
        return fee;
    }

    private double calculateFee(ParkingTicket t, long minutes, int kWh) {
        if (t.monthlyPass || t.oneDayPass) return 0.0;
        if (t.vehicle.type() == VehicleType.E_BIKE) return 25.0; // flat fee per visit
        long billableHours = (minutes + 59) / 60;
        double carRate = 30.0;
        double fee = switch (t.vehicle.type()) {
            case TWO_WHEELER -> billableHours * 10.0;
            case CAR -> billableHours * carRate;
            case ELECTRIC_CAR -> billableHours * 40.0 + Math.max(0, kWh) * 12.0;
            case DELIVERY_VAN -> {
                long overlap = overlapWithBusinessHours(t.entryTime, t.entryTime.plusMinutes(minutes));
                long extraHours = (overlap + 59) / 60;
                yield billableHours * carRate + extraHours * carRate;
            }
            case E_BIKE -> 25.0;
        };
        return fee;
    }

    /** Counts minutes overlapping 09:00–17:00, across all dates in the stay. */
    private long overlapWithBusinessHours(LocalDateTime start, LocalDateTime end) {
        long total = 0;
        for (var day = start.toLocalDate(); !day.isAfter(end.toLocalDate()); day = day.plusDays(1)) {
            LocalDateTime a = day.atTime(9, 0), b = day.atTime(17, 0);
            LocalDateTime left = start.isAfter(a) ? start : a;
            LocalDateTime right = end.isBefore(b) ? end : b;
            if (right.isAfter(left)) total += Duration.between(left, right).toMinutes();
        }
        return total;
    }

    int activeCount() { return activeTickets.size(); }
    int floorCount() { return floors.length; }

    public static void main(String[] args) {
        ParkingSystem p = new ParkingSystem(new int[]{4, 5, 3, 6, 4});
        LocalDateTime start = LocalDateTime.of(2026, 11, 18, 10, 0);
        ParkingTicket car = p.enter(new Vehicle("TN01AA1111", VehicleType.CAR), UserType.VISITOR, false, false, start);
        System.out.println("ENTRY " + car.ticketId + " floor=" + car.slot.floor + " slot=" + car.slot.number);
        System.out.println("CAR EXIT FEE Rs " + p.exit("TN01AA1111", start.plusMinutes(125), 0));
        ParkingTicket ebike = p.enter(new Vehicle("TN02BB2222", VehicleType.E_BIKE), UserType.VISITOR, false, false, start);
        System.out.println("E-BIKE EXIT FEE Rs " + p.exit("TN02BB2222", start.plusMinutes(30), 0));
        ParkingTicket van = p.enter(new Vehicle("TN03CC3333", VehicleType.DELIVERY_VAN), UserType.VISITOR, false, false, start);
        System.out.println("VAN EXIT FEE Rs " + p.exit("TN03CC3333", start.plusHours(2), 0));
        ParkingTicket pass = p.enter(new Vehicle("TN04DD4444", VehicleType.CAR), UserType.VISITOR, false, true, start);
        System.out.println("ONE-DAY PASS EXIT FEE Rs " + p.exit("TN04DD4444", start.plusHours(5), 0));
        System.out.println("ACTIVE TICKETS " + p.activeCount() + " | FLOORS " + p.floorCount());
    }
}
