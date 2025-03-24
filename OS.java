import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexDemo {
    private static final int MAX_WRITES = 5;
    private static int sharedData = 0;
    private static final Lock mutex = new ReentrantLock();

    // Writer thread
    static class Writer implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= MAX_WRITES; i++) {
                mutex.lock();
                try {
                    sharedData = i;
                    System.out.println("Writer: Wrote " + sharedData);
                } finally {
                    mutex.unlock();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Reader thread
    static class Reader implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < MAX_WRITES; i++) {
                mutex.lock();
                try {
                    System.out.println("Reader: Read " + sharedData);
                } finally {
                    mutex.unlock();
                }
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Writer());
        Thread thread2 = new Thread(new Reader());

     
        thread1.start();
        thread2.start();

       
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}