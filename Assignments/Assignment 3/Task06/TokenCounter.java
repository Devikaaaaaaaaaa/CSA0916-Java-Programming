public class TokenCounter {
    private final int[] buffer = new int[5];
    private int count = 0, in = 0, out = 0;

    public synchronized void put(int token) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[in] = token; in = (in + 1) % buffer.length; count++;
        System.out.println("Produced " + token + " | counter size: " + count);
        notifyAll();
    }
    public synchronized int take() throws InterruptedException {
        while (count == 0) wait();
        int token = buffer[out]; out = (out + 1) % buffer.length; count--;
        System.out.println("Collected " + token + " | counter size: " + count);
        notifyAll(); return token;
    }

    static class Wallet {
        private int balance;
        Wallet(int balance) { this.balance = balance; }
        int getBalance() { return balance; }
        void debit(int amount) { balance -= amount; }
        void credit(int amount) { balance += amount; }
    }
    private static final Object tieLock = new Object();
    static void transfer(Wallet from, Wallet to, int amount) {
        int a = System.identityHashCode(from), b = System.identityHashCode(to);
        if (a == b) {
            synchronized (tieLock) { synchronized (from) { synchronized (to) { move(from,to,amount); } } }
        } else {
            Wallet first = a < b ? from : to, second = a < b ? to : from;
            synchronized (first) { synchronized (second) { move(from,to,amount); } }
        }
    }
    private static void move(Wallet from, Wallet to, int amount) {
        if (amount <= 0 || from.getBalance() < amount) throw new IllegalArgumentException("Invalid transfer");
        from.debit(amount); to.credit(amount);
    }
    public static void main(String[] args) throws InterruptedException {
        TokenCounter counter = new TokenCounter(); int[] collected = new int[2];
        Thread cook = new Thread(() -> {
            try { for (int t=1; t<=20; t++) counter.put(t); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread[] students = new Thread[2];
        for (int s=0; s<2; s++) {
            final int id=s;
            students[s] = new Thread(() -> {
                try { for (int k=0; k<10; k++) collected[id] += counter.take(); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        cook.start(); for (Thread t:students) t.start();
        cook.join(); for (Thread t:students) t.join();
        System.out.println("Sum of tokens collected: " + (collected[0]+collected[1]) + " (expected 210)");
        Wallet w1=new Wallet(1000), w2=new Wallet(500);
        Thread x=new Thread(() -> transfer(w1,w2,100));
        Thread y=new Thread(() -> transfer(w2,w1,50));
        x.start(); y.start(); x.join(); y.join();
        System.out.println("Wallet total after transfers: " + (w1.getBalance()+w2.getBalance()));
    }
}