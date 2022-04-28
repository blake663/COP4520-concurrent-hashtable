import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Experiment {
    static ArrayList<Long> unthreadedTimes = new ArrayList<Long>();
    static ArrayList<Long> threadedTimes = new ArrayList<Long>();
    static ArrayList<Long> lockingTimes = new ArrayList<Long>();
    static ArrayList<Long> javaTimes = new ArrayList<Long>();

    public static void main(String[] args) throws ParseException, InterruptedException {
        int N = 250;
        for (int n = 0; n < N; n++) {
            System.out.println("//////////////////////////////////////////////////////");
            // Unthreaded
            UnthreadedExperiment standard_u = new UnthreadedExperiment();
            Thread single = new Thread(standard_u, "Single");
            single.start();
            single.join();
            // System.out.println("Finished unthreaded experiment on standard hash
            // table.\n");
            ThreadedExperiment[] standard = new ThreadedExperiment[8];
            Thread[] nonlocking = new Thread[8];

            // Nonlocking threaded
            for (int i = 0; i < 8; i++) {
                standard[i] = new ThreadedExperiment(i);
                nonlocking[i] = new Thread(standard[i], Integer.toString(i));
            }

            long startTime = System.nanoTime();
            for (int i = 0; i < 8; i++)
                nonlocking[i].start();
            for (int i = 0; i < 8; i++)
                nonlocking[i].join();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            /*
             * System.out.println(String.format(
             * "Threaded standard hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes"
             * ,
             * duration / 1000000));
             */
            threadedTimes.add(duration / 1000000);
            int insertion_failures = 0;
            int removal_failures = 0;
            for (int i = 0; i < 8; i++) {
                insertion_failures += standard[i].insertion_failures;
                removal_failures += standard[i].removal_failures;
            }
            System.out.println(
                    String.format("Nonlocking threaded insertion failed %d times, and deletion failed %d times.",
                            insertion_failures, removal_failures));
            // System.out.println("Finished threaded experiment on standard hash table.\n");

            // Locking threaded
            ThreadedExperiment2[] locking_experiment = new ThreadedExperiment2[8];
            Thread[] locking = new Thread[8];

            for (int i = 0; i < 8; i++) {
                locking_experiment[i] = new ThreadedExperiment2(i);
                locking[i] = new Thread(locking_experiment[i], Integer.toString(i));
            }

            startTime = System.nanoTime();
            for (int i = 0; i < 8; i++)
                locking[i].start();
            for (int i = 0; i < 8; i++)
                locking[i].join();
            endTime = System.nanoTime();
            duration = (endTime - startTime);

            /*
             * System.out.println(String.format(
             * "Threaded locking hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes"
             * ,
             * duration / 1000000));
             */
            lockingTimes.add(duration / 1000000);
            insertion_failures = 0;
            removal_failures = 0;
            for (int i = 0; i < 8; i++) {
                insertion_failures += locking_experiment[i].insertion_failures;
                removal_failures += locking_experiment[i].removal_failures;
            }
            System.out
                    .println(String.format("Locking threaded insertion failed %d times, and deletion failed %d times.",
                            insertion_failures, removal_failures));
            // System.out.println("Finished threaded experiment on the locking hash
            // table.");

            JavaConcurrent[] java_experiment = new JavaConcurrent[8];
            Thread[] java_locking = new Thread[8];

            for (int i = 0; i < 8; i++) {
                java_experiment[i] = new JavaConcurrent(i);
                java_locking[i] = new Thread(java_experiment[i], Integer.toString(i));
            }

            startTime = System.nanoTime();
            for (int i = 0; i < 8; i++)
                java_locking[i].start();
            for (int i = 0; i < 8; i++)
                java_locking[i].join();
            endTime = System.nanoTime();
            duration = (endTime - startTime);

            /*
             * System.out.println(String.format(
             * "Threaded locking hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes"
             * ,
             * duration / 1000000));
             */
            javaTimes.add(duration / 1000000);
            insertion_failures = 0;
            removal_failures = 0;
            for (int i = 0; i < 8; i++) {
                insertion_failures += java_experiment[i].insertion_failures;
                removal_failures += java_experiment[i].removal_failures;
            }
            System.out
                    .println(String.format("Java threaded insertion failed %d times, and deletion failed %d times.",
                            insertion_failures, removal_failures));

        }
        System.out.println(unthreadedTimes);
        System.out.println(threadedTimes);
        System.out.println(lockingTimes);
        System.out.println(javaTimes);

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("experimentalTimes.csv"));
            out.write("Unthreaded,Nonlocking,Locking,Java\n");
            for (int i = 0; i < N; i++) {
                out.write(String.format("%d,%d,%d,%d\n", unthreadedTimes.get(i), threadedTimes.get(i),
                        lockingTimes.get(i), javaTimes.get(i)));
            }

            out.close();
        } catch (IOException e) {
            System.out.println("Failed to create file");
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

        /*
         * System.out.println(String.format(
         * "Unthreaded standard hashtable took %d milliseconds to perform 64708 inserts, 129416 searches, and 64708 deletes"
         * ,
         * duration / 1000000));
         */
        System.out.println(
                String.format("Unthreaded insertion failed %d times, and deletion failed %d times.", insertion_failures,
                        removal_failures));

        Experiment.unthreadedTimes.add(duration / 1000000);
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

class JavaConcurrent implements Runnable {
    ConcurrentHashMap<Integer, Integer> threaded;
    public int insertion_failures; // How many times the insertion of a key-value failed.
    public int removal_failures; // How many times the removal of a key-value failed.
    int thread_num;

    JavaConcurrent(int thread_num) {
        this.threaded = new ConcurrentHashMap<Integer, Integer>();
        this.insertion_failures = 0;
        this.removal_failures = 0;
        this.thread_num = thread_num;
    }

    public void run() {
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            threaded.put(i, i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (!threaded.contains(i))
                insertion_failures++;
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            threaded.remove(i);
        }
        for (int i = (this.thread_num * 8089); i < (this.thread_num * 8089) + 8089; i++) {
            if (threaded.contains(i))
                removal_failures++;
        }
    }
}