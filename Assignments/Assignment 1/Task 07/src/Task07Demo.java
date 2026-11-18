abstract class Vehicle07 {
    private final String registrationNumber;
    Vehicle07(String reg) {
        if (reg == null || reg.isBlank()) throw new IllegalArgumentException("Registration required");
        registrationNumber = reg.trim().toUpperCase();
    }
    public final String getRegistrationNumber() { return registrationNumber; }
    protected abstract double ratePerHour();
    public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        int billableHours = (minutes + 59) / 60;
        return billableHours * ratePerHour();
    }
    @Override public String toString() {
        return getClass().getSimpleName() + "[" + registrationNumber + "]";
    }
    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vehicle07)) return false;
        Vehicle07 vehicle = (Vehicle07) other;
        return registrationNumber.equals(vehicle.registrationNumber);
    }
    @Override public int hashCode() { return registrationNumber.hashCode(); }
}
class Car07 extends Vehicle07 {
    Car07(String reg) { super(reg); }
    @Override protected double ratePerHour() { return 45.0; }
}
class TwoWheeler07 extends Vehicle07 {
    TwoWheeler07(String reg) { super(reg); }
    @Override protected double ratePerHour() { return 15.0; }
}
public class Task07Demo {
    public static void main(String[] args) {
        Vehicle07 car1 = new Car07("TN02B2");
        Vehicle07 car2 = new Car07("TN02B2");
        Vehicle07 bike = new TwoWheeler07("TN01A1");
        System.out.println(car1);
        System.out.println("Car fee for 130 minutes: Rs " + car1.calculateFee(130));
        System.out.println("Same registration equals: " + car1.equals(car2));
        System.out.println("Equal objects have same hash: " + (car1.hashCode() == car2.hashCode()));
        System.out.println("Different registration equals: " + car1.equals(bike));
    }
}