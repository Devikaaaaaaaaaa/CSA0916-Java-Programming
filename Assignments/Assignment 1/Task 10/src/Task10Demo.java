package task10;
import java.util.Objects;

final class ParkingRecord {
    private String registrationNumber;
    private Integer slotNumber;

    ParkingRecord(String registrationNumber, Integer slotNumber) {
        setRegistrationNumber(registrationNumber);
        setSlotNumber(slotNumber);
    }
    public String getRegistrationNumber() { return registrationNumber; }
    public Integer getSlotNumber() { return slotNumber; }
    public void setRegistrationNumber(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Registration required");
        registrationNumber = value.trim().toUpperCase();
    }
    public void setSlotNumber(Integer value) {
        if (value == null || value < 0) throw new IllegalArgumentException("Slot must be non-negative");
        slotNumber = value;
    }
    @Override public String toString() {
        return registrationNumber + " occupies slot " + slotNumber;
    }
}
public class Task10Demo {
    public static void main(String[] args) {
        ParkingRecord record = new ParkingRecord("tn01ab1234", 15);
        System.out.println(record);
        record.setSlotNumber(18);
        System.out.println("Updated: " + record);
        try { record.setSlotNumber(-1); }
        catch (IllegalArgumentException ex) { System.out.println("Invalid update rejected: " + ex.getMessage()); }
        Integer first = 1000;
        Integer second = 1000;
        System.out.println("Wrapper values compared with equals(): " + Objects.equals(first, second));
        System.out.println("Use equals(), not ==, for wrapper value comparison.");
    }
}