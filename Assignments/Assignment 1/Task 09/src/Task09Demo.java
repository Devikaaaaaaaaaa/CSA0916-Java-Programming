interface Chargeable09 {
    double chargingFee(int kWh);
    default String chargingStatus() { return "Charging facility available"; }
    static boolean isValidEnergy(int kWh) { return kWh >= 0; }
}
interface Payable09 {
    double payFee(double amount);
}
abstract class Vehicle09 {
    private final String registrationNumber;
    protected Vehicle09(String reg) {
        if (reg == null || reg.isBlank()) throw new IllegalArgumentException("Registration required");
        registrationNumber = reg;
    }
    public String getRegistrationNumber() { return registrationNumber; }
    public abstract double ratePerHour();
}
class ElectricCar09 extends Vehicle09 implements Chargeable09, Payable09 {
    ElectricCar09(String reg) { super(reg); }
    public double ratePerHour() { return 90.0; }
    public double chargingFee(int kWh) {
        if (!Chargeable09.isValidEnergy(kWh)) throw new IllegalArgumentException("Invalid kWh");
        return kWh * 3.0;
    }
    public double payFee(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Invalid amount");
        return amount;
    }
}
public class Task09Demo {
    public static void main(String[] args) {
        ElectricCar09 ev = new ElectricCar09("TN03C3");
        System.out.println("Vehicle: " + ev.getRegistrationNumber());
        System.out.println("Hourly rate: Rs " + ev.ratePerHour());
        System.out.println(ev.chargingStatus());
        System.out.println("Charging fee for 10 kWh: Rs " + ev.chargingFee(10));
        System.out.println("Payment accepted: Rs " + ev.payFee(120));
    }
}