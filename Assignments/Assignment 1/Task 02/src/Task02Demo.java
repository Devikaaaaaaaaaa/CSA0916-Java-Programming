import java.util.Scanner;

public class Task02Demo {
    private static double calculateFee(int minutes, double hourlyRate) {
        if (minutes < 0 || hourlyRate < 0) throw new IllegalArgumentException("Negative input");
        int billableHours = (minutes + 59) / 60;
        return billableHours * hourlyRate;
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter vehicle registration: ");
        String registration = input.nextLine().trim();
        System.out.print("Enter floor number: ");
        int floor = input.nextInt();
        System.out.print("Enter slot number: ");
        int slot = input.nextInt();
        System.out.print("Enter parking duration in minutes: ");
        int minutes = input.nextInt();
        System.out.print("Enter hourly rate: ");
        double rate = input.nextDouble();

        String[][] parking = new String[3][];
        parking[0] = new String[20];
        parking[1] = new String[30];
        parking[2] = new String[25];
        if (floor < 0 || floor >= parking.length || slot < 0 || slot >= parking[floor].length)
            System.out.println("Invalid floor or slot index");
        else {
            parking[floor][slot] = registration;
            System.out.println("Stored vehicle: " + parking[floor][slot]);
        }
        System.out.println("Entry timestamp type: long (System.currentTimeMillis())");
        System.out.println("Duration type: int; fee type: double");
        System.out.println("Integer division 30 / 60 = " + (30 / 60));
        System.out.println("Correct 30 / 60.0 = " + (30 / 60.0));
        System.out.printf("Rounded parking fee: Rs %.2f%n", calculateFee(minutes, rate));
        input.close();
    }
}