import java.time.LocalDateTime;
import java.util.Objects;

enum VehicleType { TWO_WHEELER, CAR, ELECTRIC_CAR }
enum UserType { STUDENT, STAFF, VISITOR }

final class Vehicle {
    private final String registrationNumber;
    private final VehicleType type;
    private final LocalDateTime entryTime;

    Vehicle(String registrationNumber, VehicleType type, LocalDateTime entryTime) {
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("Registration number is required");
        this.registrationNumber = registrationNumber.trim().toUpperCase();
        this.type = Objects.requireNonNull(type);
        this.entryTime = Objects.requireNonNull(entryTime);
    }
    String getRegistrationNumber() { return registrationNumber; }
    VehicleType getType() { return type; }
    LocalDateTime getEntryTime() { return entryTime; }
}
final class ParkingSlot {
    private final int floorNumber, slotNumber;
    private Vehicle parkedVehicle;
    ParkingSlot(int floorNumber, int slotNumber) {
        if (floorNumber < 0 || slotNumber < 0) throw new IllegalArgumentException();
        this.floorNumber = floorNumber; this.slotNumber = slotNumber;
    }
    boolean isFree() { return parkedVehicle == null; }
    void park(Vehicle v) {
        if (!isFree()) throw new IllegalStateException("Slot is occupied");
        parkedVehicle = Objects.requireNonNull(v);
    }
    Vehicle release() {
        if (isFree()) throw new IllegalStateException("Slot is already free");
        Vehicle v = parkedVehicle; parkedVehicle = null; return v;
    }
    int getFloorNumber() { return floorNumber; }
    int getSlotNumber() { return slotNumber; }
}
final class ParkingTicket {
    private static int nextTicketNumber = 1001;
    private final int ticketNumber;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    ParkingTicket(Vehicle vehicle, ParkingSlot slot) {
        this.ticketNumber = nextTicketNumber++;
        this.vehicle = Objects.requireNonNull(vehicle);
        this.slot = Objects.requireNonNull(slot);
    }
    int getTicketNumber() { return ticketNumber; }
    Vehicle getVehicle() { return vehicle; }
    ParkingSlot getSlot() { return slot; }
}
final class ParkingUser {
    private final String userId;
    private final UserType userType;
    private final boolean validMonthlyPass;
    ParkingUser(String id, UserType type, boolean pass) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("User ID required");
        userId = id; userType = Objects.requireNonNull(type); validMonthlyPass = pass;
    }
    boolean parksFree() {
        return validMonthlyPass && (userType == UserType.STUDENT || userType == UserType.STAFF);
    }
}
public class Task01Demo {
    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle("TN01AB1234", VehicleType.CAR, LocalDateTime.now());
        ParkingSlot slot = new ParkingSlot(0, 12);
        slot.park(vehicle);
        ParkingTicket ticket = new ParkingTicket(vehicle, slot);
        ParkingUser student = new ParkingUser("STU101", UserType.STUDENT, true);
        System.out.println("Vehicle: " + vehicle.getRegistrationNumber() + " / " + vehicle.getType());
        System.out.println("Slot: floor " + slot.getFloorNumber() + ", number " + slot.getSlotNumber());
        System.out.println("Ticket: " + ticket.getTicketNumber());
        System.out.println("Student monthly pass: " + student.parksFree());
        System.out.println("Occupied slot cannot accept another vehicle: " + !slot.isFree());
    }
}