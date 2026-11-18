import java.time.LocalDateTime;
import java.util.Objects;

final class ParkingTicket04 {
    private final int ticketNumber;
    private final String registrationNumber;
    private final int floorNumber;
    private final int slotNumber;
    private final LocalDateTime entryTime;

    public ParkingTicket04(int number, String registration, int floor, int slot) {
        this(number, registration, floor, slot, LocalDateTime.now());
    }
    public ParkingTicket04(int number, String registration) {
        this(number, registration, -1, -1, LocalDateTime.now());
    }
    public ParkingTicket04(int number, String registration, int floor, int slot, LocalDateTime entry) {
        if (number <= 0) throw new IllegalArgumentException("Ticket number must be positive");
        if (registration == null || registration.isBlank()) throw new IllegalArgumentException("Registration required");
        if ((floor < 0) != (slot < 0)) throw new IllegalArgumentException("Floor and slot must both be supplied");
        ticketNumber = number;
        registrationNumber = registration.trim().toUpperCase();
        floorNumber = floor;
        slotNumber = slot;
        entryTime = Objects.requireNonNull(entry);
    }
    public int getTicketNumber() { return ticketNumber; }
    public String getRegistrationNumber() { return registrationNumber; }
    public int getFloorNumber() { return floorNumber; }
    public int getSlotNumber() { return slotNumber; }
    public LocalDateTime getEntryTime() { return entryTime; }
    @Override public String toString() {
        return "Ticket{number=" + ticketNumber + ", vehicle='" + registrationNumber
                + "', floor=" + floorNumber + ", slot=" + slotNumber + ", entry=" + entryTime + "}";
    }
}
public class Task04Demo {
    public static void main(String[] args) {
        ParkingTicket04 standard = new ParkingTicket04(101, "TN01AA1111", 0, 4);
        ParkingTicket04 pending = new ParkingTicket04(102, "TN02BB2222");
        ParkingTicket04 explicitTime = new ParkingTicket04(103, "TN03CC3333", 1, 2,
                LocalDateTime.of(2026, 11, 18, 9, 30));
        System.out.println(standard);
        System.out.println(pending);
        System.out.println(explicitTime);
        System.out.println("Constructor chaining: overloaded constructors delegate to the full constructor.");
    }
}