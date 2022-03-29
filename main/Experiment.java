import java.text.ParseException;

public class Experiment {
    public static void main(String[] args) throws ParseException, InterruptedException {
        for (int n = 0; n < 10; n++) {
            System.out.println("\n////////////////////////////////////////////////////////////////////////////////\n");
            UnthreadedExperiment standard_u = new UnthreadedExperiment();
            Thread single = new Thread(standard_u, "Single");
            single.start();
            single.join();
            System.out.println("Finished unthreaded experiment on standard hash table.\n");
    /*
            ThreadedExperiment[] standard = new ThreadedExperiment[8];
            Thread[] nonlocking = new Thread[8];

            for (int i = 0; i < 8; i++) {
                standard[i] = new ThreadedExperiment(i);
                nonlocking[i] = new Thread(standard[i], Integer.toString(i));
            }
            
            long startTime = System.nanoTime();
            for (int i = 0; i < 8; i++) nonlocking[i].start();
            for (int i = 0; i < 8; i++) nonlocking[i].join();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            System.out.println(String.format("Threaded standard hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes", duration/1000000));
            int insertion_failures = 0;
            int removal_failures = 0;
            for (int i = 0; i < 8; i++) {
                insertion_failures += standard[i].insertion_failures;
                removal_failures += standard[i].removal_failures;
            }
            System.out.println(String.format("Insertion failed %d times, and deletion failed %d times.", insertion_failures, removal_failures));
            System.out.println("Finished threaded experiment on standard hash table.\n");
    */

            ThreadedExperiment[] locking_experiment = new ThreadedExperiment[8];
            Thread[] locking = new Thread[8];

            for (int i = 0; i < 8; i++) {
                locking_experiment[i] = new ThreadedExperiment(i);
                locking[i] = new Thread(locking_experiment[i], Integer.toString(i));
            }

            long startTime = System.nanoTime();
            for (int i = 0; i < 8; i++) locking[i].start();
            for (int i = 0; i < 8; i++) locking[i].join();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            System.out.println(String.format("Threaded locking hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes", duration/1000000));
            int insertion_failures = 0;
            int removal_failures = 0;
            for (int i = 0; i < 8; i++) {
                insertion_failures += locking_experiment[i].insertion_failures;
                removal_failures += locking_experiment[i].removal_failures;
            }
            System.out.println(String.format("Insertion failed %d times, and deletion failed %d times.", insertion_failures, removal_failures));
            System.out.println("Finished threaded experiment on the locking hash table.");
        }
    }
}

class UnthreadedExperiment implements Runnable {
    HashTable standard_unthreaded;
    int insertion_failures; // How many times the insertion of a key-value failed.
    int removal_failures; // How many times the removal of a key-value failed.

    UnthreadedExperiment() {
        this.standard_unthreaded = new HashTable();
        this.insertion_failures  = 0;
        this.removal_failures = 0;
    }

    public void run() {
        long startTime = System.nanoTime();

        for (int i = 0; i < 64708; i++) {
            standard_unthreaded.put(i);
        }
        for (int i = 0; i < 64708; i++) {
            if (!standard_unthreaded.search(i)) insertion_failures++;
        }
        for (int i = 0; i < 64708; i++) {
            standard_unthreaded.remove(i);
        }
        for (int i = 0; i < 64708; i++) {
            if (standard_unthreaded.search(i)) removal_failures++;
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println(String.format("Unthreaded standard hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes", duration/1000000));
        System.out.println(String.format("Insertion failed %d times, and deletion failed %d times.", insertion_failures, removal_failures));
    }
}

class ThreadedExperiment implements Runnable {
    HashTable standard_unthreaded;
    public int insertion_failures; // How many times the insertion of a key-value failed.
    public int removal_failures; // How many times the removal of a key-value failed.
    int thread_num;

    ThreadedExperiment(int thread_num) {
        this.standard_unthreaded = new HashTable();
        this.insertion_failures  = 0;
        this.removal_failures = 0;
        this.thread_num = thread_num;
    }

    public void run() {
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            standard_unthreaded.put(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (!standard_unthreaded.search(i)) insertion_failures++;
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            standard_unthreaded.remove(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (standard_unthreaded.search(i)) removal_failures++;
        }
    }
}

class ThreadedExperiment2 implements Runnable {
    LockingHashTable standard_unthreaded;
    public int insertion_failures; // How many times the insertion of a key-value failed.
    public int removal_failures; // How many times the removal of a key-value failed.
    int thread_num;

    ThreadedExperiment2(int thread_num) {
        this.standard_unthreaded = new LockingHashTable();
        this.insertion_failures  = 0;
        this.removal_failures = 0;
        this.thread_num = thread_num;
    }

    public void run() {
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            standard_unthreaded.put(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (!standard_unthreaded.search(i)) insertion_failures++;
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            standard_unthreaded.remove(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (standard_unthreaded.search(i)) removal_failures++;
        }
    }
}