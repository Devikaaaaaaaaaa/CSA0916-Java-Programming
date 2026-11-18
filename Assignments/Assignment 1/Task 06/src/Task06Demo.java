abstract class Vehicle06 {
    private final String registrationNumber;
    Vehicle06(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("Registration required");
        this.registrationNumber = registrationNumber;
        System.out.println("Vehicle constructor: " + registrationNumber);
    }
    public String getRegistrationNumber() { return registrationNumber; }
    public abstract double ratePerHour();
}
class TwoWheeler06 extends Vehicle06 {
    TwoWheeler06(String reg) { super(reg); System.out.println("TwoWheeler constructor"); }
    public double ratePerHour() { return 15.0; }
}
class Car06 extends Vehicle06 {
    Car06(String reg) { super(reg); System.out.println("Car constructor"); }
    public double ratePerHour() { return 45.0; }
}
class ElectricCar06 extends Car06 {
    ElectricCar06(String reg) { super(reg); System.out.println("ElectricCar constructor"); }
}
abstract class CampusUser06 {
    private final String userId;
    CampusUser06(String id) { userId = id; }
    public String getUserId() { return userId; }
    public abstract boolean hasFreeParking();
}
class Student06 extends CampusUser06 {
    private final boolean monthlyPass;
    Student06(String id, boolean pass) { super(id); monthlyPass = pass; }
    public boolean hasFreeParking() { return monthlyPass; }
}
class Visitor06 extends CampusUser06 {
    Visitor06(String id) { super(id); }
    public boolean hasFreeParking() { return false; }
}
public class Task06Demo {
    public static void main(String[] args) {
        Vehicle06 vehicle = new ElectricCar06("TN03C3");
        System.out.println("Electric vehicle rate inherited from Car: Rs " + vehicle.ratePerHour());
        CampusUser06 student = new Student06("STU100", true);
        CampusUser06 visitor = new Visitor06("VIS100");
        System.out.println("Student free parking: " + student.hasFreeParking());
        System.out.println("Visitor free parking: " + visitor.hasFreeParking());
    }
}