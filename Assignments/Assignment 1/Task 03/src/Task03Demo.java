import java.util.Arrays;

public class Task03Demo {
    private final String[][] slots;

    public Task03Demo(int[] slotsPerFloor) {
        if (slotsPerFloor == null) throw new IllegalArgumentException("Floor capacities are required");
        slots = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            if (slotsPerFloor[f] < 0) throw new IllegalArgumentException("Negative floor capacity");
            slots[f] = new String[slotsPerFloor[f]];
        }
    }

    // Returns {floor, slot} for the first free slot, or null if full.
    public int[] allocate(String vehicleNo) {
        if (vehicleNo == null || vehicleNo.isBlank())
            throw new IllegalArgumentException("Vehicle number is required");
        for (int floor = 0; floor < slots.length; floor++) {
            for (int slot = 0; slot < slots[floor].length; slot++) {
                if (slots[floor][slot] == null) {
                    slots[floor][slot] = vehicleNo;
                    return new int[] { floor, slot };
                }
            }
        }
        return null;
    }

    // Frees an occupied slot. Invalid or already-free positions return false.
    public boolean release(int floor, int slot) {
        if (floor < 0 || floor >= slots.length) return false;
        if (slot < 0 || slot >= slots[floor].length) return false;
        if (slots[floor][slot] == null) return false;
        slots[floor][slot] = null;
        return true;
    }

    public static double calculateFee(int minutes, double hourlyRate) {
        if (minutes < 0 || hourlyRate < 0) throw new IllegalArgumentException("Invalid fee input");
        int hours = (minutes + 59) / 60;
        return hours * hourlyRate;
    }

    public static void main(String[] args) {
        Task03Demo p = new Task03Demo(new int[] { 2, 1 });
        System.out.println(Arrays.toString(p.allocate("TN01AB1234")));
        System.out.println(Arrays.toString(p.allocate("TN02CD5678")));
        System.out.println(Arrays.toString(p.allocate("TN03EF9012")));
        System.out.println(p.allocate("TN04GH3456"));
        System.out.println(p.release(0, 1) + " " + p.release(0, 1) + " " + p.release(5, 0));
        System.out.println(Arrays.toString(p.allocate("TN04GH4444")));
    }
}