import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class ParkingTicket05 {
    private static int nextNumber = 5001;
    private static int activeCount = 0;
    private final int ticketNumber;
    private final String registrationNumber;
    private boolean active;

    static {
        System.out.println("Parking ticket service initialized");
    }
    ParkingTicket05(String registration) {
        if (registration == null || registration.isBlank()) throw new IllegalArgumentException("Registration required");
        ticketNumber = nextNumber++;
        registrationNumber = registration;
        active = true;
        activeCount++;
    }
    int getTicketNumber() { return ticketNumber; }
    String getRegistrationNumber() { return registrationNumber; }
    void close() {
        if (active) { active = false; activeCount--; }
    }
    static int getActiveCount() { return activeCount; }
    static int getNextNumber() { return nextNumber; }
}
public class Task05Demo {
    private static ParkingTicket05 createTicket(String reg) { return new ParkingTicket05(reg); }
    private static void printTicket(ParkingTicket05 ticket) {
        System.out.println("Ticket passed to method: " + ticket.getTicketNumber()
                + " / " + ticket.getRegistrationNumber());
    }
    public static void main(String[] args) {
        List<ParkingTicket05> tickets = new ArrayList<>();
        ParkingTicket05 first = createTicket("TN01AA1111");
        ParkingTicket05 second = createTicket("TN02BB2222");
        tickets.add(first); tickets.add(second);
        printTicket(first);
        System.out.println("Active tickets: " + ParkingTicket05.getActiveCount());
        first.close();
        tickets.remove(first);
        first = null;
        System.out.println("After first vehicle exits, active tickets: " + ParkingTicket05.getActiveCount());
        System.out.println("Next ticket number: " + ParkingTicket05.getNextNumber());
        System.out.println("The first ticket becomes eligible for GC only when no reachable references remain.");
        second.close();
        tickets.clear();
        System.out.println("Final active tickets: " + ParkingTicket05.getActiveCount());
    }
}