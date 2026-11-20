import java.util.Scanner;

/** Demonstrates separation between a fee-payment view and business service. */
public class FeePaymentDemo {
    static class FeeService {
        String pay(String studentId, String amountText) {
            if (studentId == null || !studentId.matches("[A-Za-z0-9-]{3,20}"))
                throw new IllegalArgumentException("Enter a valid student ID.");
            double amount;
            try { amount = Double.parseDouble(amountText); }
            catch (NumberFormatException ex) { throw new IllegalArgumentException("Enter a numeric amount."); }
            if (!Double.isFinite(amount) || amount <= 0)
                throw new IllegalArgumentException("Amount must be greater than zero.");
            return String.format("Payment accepted for %s. Amount: Rs. %.2f", studentId, amount);
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        FeeService service = new FeeService();
        System.out.print("Student ID: "); String id = in.nextLine();
        System.out.print("Fee amount: "); String amount = in.nextLine();
        try { System.out.println(service.pay(id, amount)); }
        catch (IllegalArgumentException ex) { System.out.println("Payment failed: " + ex.getMessage()); }
    }
}