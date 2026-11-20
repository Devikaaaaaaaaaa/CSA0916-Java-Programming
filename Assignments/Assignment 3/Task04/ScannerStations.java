public class ScannerStations {
    static class ThreadStation extends Thread {
        private final int scripts;
        ThreadStation(String name, int scripts) { super(name); this.scripts = scripts; }
        public void run() {
            try {
                for (int i = 1; i <= scripts; i++) {
                    System.out.println(getName() + " scanned script " + i);
                    Thread.sleep(40);
                }
                System.out.println(getName() + " completed " + scripts + " scripts.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(getName() + " was interrupted.");
            }
        }
    }
    static class RunnableStation implements Runnable {
        private final String name; private final int scripts;
        RunnableStation(String name, int scripts) { this.name = name; this.scripts = scripts; }
        public void run() {
            try {
                for (int i = 1; i <= scripts; i++) {
                    System.out.println(name + " scanned script " + i); Thread.sleep(30);
                }
                System.out.println(name + " completed " + scripts + " scripts.");
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = {new ThreadStation("Station-1",2),
            new Thread(new RunnableStation("Station-2",2)),
            new Thread(new RunnableStation("Station-3",2)),
            new Thread(new RunnableStation("Station-4",2))};
        for (Thread t : stations) t.start();
        for (Thread t : stations) t.join();
        System.out.println("All scanner stations completed.");
        for (Thread t : stations) System.out.println(t.getName() + " alive: " + t.isAlive());
    }
}