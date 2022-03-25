import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class Experiment {
    public static void main(String[] args) throws ParseException, InterruptedException {
        long startTime = System.nanoTime();
        HashTable standard_unthreaded = new HashTable();
        int su_insertion_failures  = 0; // How many times the insertion of a key-value failed.
        int su_removal_failures = 0; // How many times the removal of a key-value failed.

        for (int i = 0; i < 809; i++) {
            standard_unthreaded.put(i);
        }
        for (int i = 0; i < 809; i++) {
            if (!standard_unthreaded.search(i)) su_insertion_failures++;
        }
        for (int i = 0; i < 809; i++) {
            standard_unthreaded.remove(i);
        }
        for (int i = 0; i < 809; i++) {
            if (standard_unthreaded.search(i)) su_removal_failures++;
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println(String.format("Unthreaded standard hashtable took %d nanoseconds to perform 809 inserts, 1618 searchs, and 809 deletes", duration));
        System.out.println(String.format("Insertion failed %d times, and deletion failed %d times.\n", su_insertion_failures, su_removal_failures));

        startTime = System.nanoTime();
    }
}