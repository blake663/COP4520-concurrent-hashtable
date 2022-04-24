import java.util.concurrent.locks.Lock;

public class FineLockingHashTable {
    public static final int ARR_SIZE = 809; // The maximum capacity of a HashTable
    private Integer[] table = new Integer[ARR_SIZE]; // Where values are stored
    private boolean[] cleans = new boolean[ARR_SIZE]; // Where clean indices are stored
    private Lock[] locks = new Lock[ARR_SIZE]; // locks for each item
    private int capacity;

    public FineLockingHashTable() {
        // Generate arrays, each with default values. Set capacity to 0
        for (int i = 0; i < ARR_SIZE; i++) {
            table[i] = null;
            cleans[i] = true;
        }
        capacity = 0;
    }

    // Add an item to the hash table
    public void put(int item) {
        // If capacity if full, do not add
        if (capacity >= ARR_SIZE)
            return;

        // Get key
        int key = hash(item);

        // Quadratically probe for empty indices
        int i = 0;
        int new_key = (key + (i * i)) % ARR_SIZE;
        while (true) {
            if (table[new_key] == null) {
                locks[new_key].lock();
                // Make sure still null
                if (table[new_key] == null) {
                    table[new_key] = item;
                    cleans[new_key] = false;
                    locks[new_key].unlock();
                    break;
                }
                locks[new_key].unlock();
            }

            i++;
            new_key = (new_key + (i * i)) % ARR_SIZE;
        }

        // Increase capacity
        capacity++;
    }

    // Search the hash table for a specific item, return true if found, else false
    public boolean search(int item) {
        int key = hash(item);
        int i = 0;
        int new_key = (key + (i * i)) % ARR_SIZE;
        // Quadratically probe for the item
        // Dirty indices are considered to be a collision, the item may of been probed
        // farther down
        while (!cleans[new_key]) {
            if (item == table[new_key]) {
                return true;
            }
            i++;
            new_key = (new_key + (i * i)) % ARR_SIZE;
            if (i >= ARR_SIZE)
                break;
        }

        return false;
    }

    // Removes an item from the hash table
    public boolean remove(int item) {
        int key = hash(item);
        int i = 0;
        int new_key = (key + (i * i)) % ARR_SIZE;

        // If item not in hash table, no need to look for it again
        if (!search(item)) {
            return false;
        }

        // Quadratically probe for the item
        // Dirty indices are considered to be a collision, the item may of been probed
        // farther down
        while (!cleans[new_key]) {
            if (item == table[new_key]) {
                locks[new_key].lock();
                table[new_key] = null;
                locks[new_key].unlock();
                break;
            }
            i++;
            new_key = (new_key + (i * i)) % ARR_SIZE;
        }

        // Decrease capacity
        capacity--;
        return true;
    }

    // Returns amount of items in hash table
    public int getSize() {
        return this.capacity;
    }

    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    // Need to make a better hash function
    private int hash(int item) {
        return item % 101;
    }

    // Prints the hash table in the same format as Java's own HashTable
    @Override
    public String toString() {
        String s = "{";

        int index = 0, found = 0;
        while (found <= capacity && index < ARR_SIZE) {
            if (table[index] != null) {
                s += hash(table[index]) + "=" + table[index] + ", ";
                found++;
            }
            index++;
        }
        s += "}";
        s = s.replace(", }", "}\n");

        return s;
    }
}
