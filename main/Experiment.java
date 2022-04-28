import java.text.ParseException;
import java.util.Random;

public class Experiment {
    public static void main(String[] args) throws ParseException, InterruptedException {
        for (int n = 0; n < 10; n++) {
            ThreadedRandomExperiment[] standard = new ThreadedRandomExperiment[8];
            Thread[] nonlocking = new Thread[8];
            System.out.println("\n////////////////////////////////////////////////////////////////////////////////\n");
            // Nonlocking threaded
            for (int i = 0; i < 8; i++) {
                standard[i] = new ThreadedRandomExperiment(i);
                nonlocking[i] = new Thread(standard[i], Integer.toString(i));
            }

            long startTime = System.nanoTime();
            for (int i = 0; i < 8; i++) {
                nonlocking[i].start();
            }
            for (int i = 0; i < 8; i++) {
                nonlocking[i].join();
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            System.out.println(String.format(
                    "Randomized threaded standard hashtable took %d milliseconds", duration / 1000000));
            int insertion_failures = 0;
            int removal_failures = 0;
            for (int i = 0; i < 8; i++) {
                insertion_failures += standard[i].insertion_failures;
                removal_failures += standard[i].removal_failures;
            }
            System.out.println(String.format("Insertion failed %d times, and deletion failed %d times.",
                    insertion_failures, removal_failures));
            System.out.println("Finished threaded experiment on standard hash table.\n");
        }
    }
}

class UnthreadedExperiment implements Runnable {
    HashTable standard_unthreaded;
    int insertion_failures; // How many times the insertion of a key-value failed.
    int removal_failures; // How many times the removal of a key-value failed.

    UnthreadedExperiment() {
        this.standard_unthreaded = new HashTable();
        this.insertion_failures = 0;
        this.removal_failures = 0;
    }

    public void run() {
        long startTime = System.nanoTime();

        for (int i = 0; i < 64708; i++) {
            standard_unthreaded.put(i);
        }
        for (int i = 0; i < 64708; i++) {
            if (!standard_unthreaded.search(i))
                insertion_failures++;
        }
        for (int i = 0; i < 64708; i++) {
            standard_unthreaded.remove(i);
        }
        for (int i = 0; i < 64708; i++) {
            if (standard_unthreaded.search(i))
                removal_failures++;
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println(String.format(
                "Unthreaded standard hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes",
                duration / 1000000));
        System.out.println(String.format("Insertion failed %d times, and deletion failed %d times.", insertion_failures,
                removal_failures));
    }
}

class ThreadedExperiment implements Runnable {
    HashTable standard_unthreaded;
    public int insertion_failures; // How many times the insertion of a key-value failed.
    public int removal_failures; // How many times the removal of a key-value failed.
    int thread_num;

    ThreadedExperiment(int thread_num) {
        this.standard_unthreaded = new HashTable();
        this.insertion_failures = 0;
        this.removal_failures = 0;
        this.thread_num = thread_num;
    }

    public void run() {
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            standard_unthreaded.put(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (!standard_unthreaded.search(i))
                insertion_failures++;
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            standard_unthreaded.remove(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (standard_unthreaded.search(i))
                removal_failures++;
        }
    }
}

class ThreadedExperiment2 implements Runnable {

    LockingHashTable threaded;
    public int insertion_failures; // How many times the insertion of a key-value failed.
    public int removal_failures; // How many times the removal of a key-value failed.
    int thread_num;

    ThreadedExperiment2(int thread_num) {
        this.threaded = new LockingHashTable();
        this.insertion_failures = 0;
        this.removal_failures = 0;
        this.thread_num = thread_num;
    }

    public void run() {
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            threaded.put(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (!threaded.search(i))
                insertion_failures++;
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            threaded.remove(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (threaded.search(i))
                removal_failures++;
        }
    }
}

class ThreadedRandomExperiment implements Runnable {
    HashTable standard_unthreaded;
    public int insertion_failures; // How many times the insertion of a key-value failed.
    public int removal_failures; // How many times the removal of a key-value failed.
    int thread_num;
    Random random;

    ThreadedRandomExperiment(int thread_num) {
        this.standard_unthreaded = new HashTable();
        this.insertion_failures = 0;
        this.removal_failures = 0;
        this.thread_num = thread_num;
        this.random = new Random();
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            int randomFunc = random.nextInt(2);
            int num = random.nextInt();
            switch (randomFunc) {
                case 0:
                    standard_unthreaded.put(num);
                    if (!standard_unthreaded.search(num)) {
                        insertion_failures++;
                    }
                    break;
                case 1:
                    standard_unthreaded.remove(num);
                    if (!standard_unthreaded.search(num)) {
                        removal_failures++;
                    }
                    break;
            }
        }
    }
}