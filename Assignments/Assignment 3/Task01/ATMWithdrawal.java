import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATMWithdrawal {
    private static final double BALANCE = 12000.00;

    static void withdraw(int accountNumber, double amount) throws IOException {
        if (accountNumber <= 0) throw new IllegalArgumentException("Account number must be positive.");
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (amount > BALANCE) throw new IllegalArgumentException("Insufficient balance.");
        if (amount % 100 != 0) throw new IllegalArgumentException("Enter an amount in multiples of 100.");
        System.out.printf("Withdrawal successful. Cash dispensed: Rs. %.2f%n", amount);
        System.out.printf("Remaining balance: Rs. %.2f%n", BALANCE - amount);
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter account number: ");
            int account = input.nextInt();
            System.out.print("Enter withdrawal amount: ");
            double amount = input.nextDouble();
            if (!Files.exists(Path.of("card-reader.cfg")))
                throw new IOException("Card-reader configuration file is missing.");
            withdraw(account, amount);
        } catch (InputMismatchException e) {
            System.out.println("Input error: enter numeric values only.");
        } catch (IllegalArgumentException e) {
            System.out.println("Transaction rejected: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Device error: " + e.getMessage());
        } finally {
            System.out.println("ATM session closed. Please take your card.");
        }
    }
}