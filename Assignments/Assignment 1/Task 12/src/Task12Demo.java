import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

interface PricingPolicy12 {
    double calculateFee(int minutes, LocalTime entry, LocalTime exit);
}
final class CarPricing12 implements PricingPolicy12 {
    public double calculateFee(int minutes, LocalTime entry, LocalTime exit) {
        return ((minutes + 59) / 60) * 45.0;
    }
}
final class EBikePricing12 implements PricingPolicy12 {
    public double calculateFee(int minutes, LocalTime entry, LocalTime exit) {
        return 25.0;
    }
}
final class DeliveryVanPricing12 implements PricingPolicy12 {
    public double calculateFee(int minutes, LocalTime entry, LocalTime exit) {
        int hours = (minutes + 59) / 60;
        boolean peak = entry.isBefore(LocalTime.of(17, 0))
                && exit.isAfter(LocalTime.of(9, 0));
        return hours * 45.0 * (peak ? 2.0 : 1.0);
    }
}
final class VisitorPass12 {
    private final String visitorId;
    private final LocalTime validUntil;
    VisitorPass12(String visitorId, LocalTime validUntil) {
        this.visitorId = visitorId; this.validUntil = validUntil;
    }
    boolean isValidAt(LocalTime time) { return !time.isAfter(validUntil); }
    String getVisitorId() { return visitorId; }
}
public class Task12Demo {
    public static void main(String[] args) {
        int[] slotsPerFloor = { 20, 25, 18, 30, 22 };
        Map<String, PricingPolicy12> policies = new HashMap<>();
        policies.put("CAR", new CarPricing12());
        policies.put("E_BIKE", new EBikePricing12());
        policies.put("DELIVERY_VAN", new DeliveryVanPricing12());
        System.out.println("Five floor capacities: " + Arrays.toString(slotsPerFloor));
        System.out.println("Car for 120 minutes: Rs " + policies.get("CAR").calculateFee(120,
                LocalTime.of(8, 0), LocalTime.of(10, 0)));
        System.out.println("E-bike flat fee: Rs " + policies.get("E_BIKE").calculateFee(90,
                LocalTime.of(10, 0), LocalTime.of(11, 30)));
        System.out.println("Delivery van during peak: Rs " + policies.get("DELIVERY_VAN").calculateFee(120,
                LocalTime.of(10, 0), LocalTime.of(12, 0)));
        VisitorPass12 pass = new VisitorPass12("VIS100", LocalTime.of(23, 59));
        System.out.println("One-day visitor pass valid at 18:00: " + pass.isValidAt(LocalTime.of(18, 0)));
        System.out.println("Decision: MODIFY existing design using jagged arrays and pricing strategies.");
    }
}