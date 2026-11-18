import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

enum VehicleKind11 { TWO_WHEELER, CAR, ELECTRIC_CAR }
final class Vehicle11 {
    private final String registration;
    private final VehicleKind11 kind;
    Vehicle11(String registration, VehicleKind11 kind) {
        if (registration == null || registration.isBlank()) throw new IllegalArgumentException("Registration required");
        this.registration = registration.trim().toUpperCase();
        this.kind = kind;
    }
    String getRegistration() { return registration; }
    VehicleKind11 getKind() { return kind; }
}
final class ParkingTicket11 {
    private static int nextNumber = 10001;
    private final int ticketNumber = nextNumber++;
    private final Vehicle11 vehicle;
    private final int floor, slot;
    private final LocalDateTime entryTime;
    ParkingTicket11(Vehicle11 v, int f, int s, LocalDateTime time) {
        vehicle = v; floor = f; slot = s; entryTime = time;
    }
    int getTicketNumber() { return ticketNumber; }
    Vehicle11 getVehicle() { return vehicle; }
}
final class ParkingSystem11 {
    private final String[][] slots;
    private final Map<String, ParkingTicket11> active = new HashMap<>();
    ParkingSystem11(int[] capacities) {
        slots = new String[capacities.length][];
        for (int i = 0; i < capacities.length; i++) {
            if (capacities[i] < 0) throw new IllegalArgumentException("Negative capacity");
            slots[i] = new String[capacities[i]];
        }
    }
    ParkingTicket11 enter(String registration, VehicleKind11 kind, LocalDateTime time) {
        String key = registration.trim().toUpperCase();
        if (active.containsKey(key)) throw new IllegalArgumentException("Duplicate registration");
        for (int f = 0; f < slots.length; f++)
            for (int s = 0; s < slots[f].length; s++)
                if (slots[f][s] == null) {
                    Vehicle11 v = new Vehicle11(key, kind);
                    ParkingTicket11 t = new ParkingTicket11(v, f, s, time);
                    slots[f][s] = key;
                    active.put(key, t);
                    return t;
                }
        return null;
    }
    double exit(String registration, LocalDateTime exitTime, boolean validMonthlyPass) {
        String key = registration.trim().toUpperCase();
        ParkingTicket11 t = active.get(key);
        if (t == null) throw new IllegalArgumentException("No active ticket");
        active.remove(key);
        slots[t.floor][t.slot] = null;
        if (validMonthlyPass) return 0.0;
        long minutes = Math.max(0, Duration.between(t.entryTime, exitTime).toMinutes());
        int hours = (int) ((minutes + 59) / 60);
        double rate = switch (t.vehicle.getKind()) {
            case TWO_WHEELER -> 15.0;
            case CAR -> 45.0;
            case ELECTRIC_CAR -> 90.0;
        };
        return hours * rate;
    }
    boolean isValidSlot(int floor, int slot) {
        return floor >= 0 && floor < slots.length && slot >= 0 && slot < slots[floor].length;
    }
}
public class Task11Demo {
    public static void main(String[] args) {
        ParkingSystem11 system = new ParkingSystem11(new int[] { 1, 1 });
        LocalDateTime start = LocalDateTime.of(2026, 11, 18, 9, 0);
        ParkingTicket11 first = system.enter("TN01AA1111", VehicleKind11.CAR, start);
        System.out.println("Entry ticket: " + first.getTicketNumber() + ", vehicle=" + first.getVehicle().getRegistration());
        System.out.println("60-minute car fee: Rs " + system.exit("TN01AA1111", start.plusMinutes(60), false));
        ParkingTicket11 last = system.enter("TN02BB2222", VehicleKind11.TWO_WHEELER, start);
        System.out.println("Last free slot allocated: " + (last != null));
        System.out.println("Full facility rejects next vehicle: " + (system.enter("TN03CC3333", VehicleKind11.CAR, start) == null));
        try { system.enter("TN02BB2222", VehicleKind11.TWO_WHEELER, start); }
        catch (IllegalArgumentException ex) { System.out.println("Duplicate registration rejected"); }
        try { system.exit("UNKNOWN", start.plusMinutes(1), false); }
        catch (IllegalArgumentException ex) { System.out.println("Unknown vehicle exit rejected"); }
        System.out.println("Invalid slot number rejected: " + !system.isValidSlot(0, 99));
        System.out.println("Valid monthly pass fee: Rs " + system.exit("TN02BB2222", start, true));
    }
}