public class LockingHashTable {
    public static final int ARR_SIZE = 64708; // The maximum capacity of a HashTable
    private int[] table = new int[ARR_SIZE]; // Where values are stored
    private boolean[] nulls = new boolean[ARR_SIZE]; // If a value is null, it is true at the same index here
    private boolean[] cleans = new boolean[ARR_SIZE]; // Where clean indices are stored
    private int capacity;

    public LockingHashTable() {
        // Generate arrays, each with default values. Set capacity to 0
        for (int i = 0; i < ARR_SIZE; i++) {
            table[i] = 0;
            nulls[i] = true;
            cleans[i] = true;
        }
        capacity = 0;
    }

    // Add an item to the hash table
    public synchronized void put(int item) {
        // If capacity if full, do not add
        if (capacity >= ARR_SIZE)
            return;

        // Get key
        int key = hash(item);

        // Check if first try addition
        if (nulls[key]) {
            table[key] = item;
            nulls[key] = false;
            cleans[key] = false;
        }
        // Otherwise, quadratically probe for empty indices
        else {
            int i = 1;
            int new_key = (key + i) % ARR_SIZE;
            while (!nulls[new_key]) {
                i++;
                new_key = (new_key + i) % ARR_SIZE;
            }
            table[new_key] = item;
            nulls[new_key] = false;
            cleans[new_key] = false;
        }

        // Increase capacity
        capacity++;
    }

    // Search the hash table for a specific item, return true if found, else false
    public synchronized boolean search(int item) {
        int key = hash(item);
        int i = 0;
        int new_key = (key + i) % ARR_SIZE;
        // Quadratically probe for the item
        // Dirty indices are considered to be a collision, the item may of been probed
        // farther down
        while (!cleans[new_key]) {
            if (item == table[new_key] && !nulls[new_key]) {
                return true;
            }
            i++;
            new_key = (new_key + i) % ARR_SIZE;
            if (i >= ARR_SIZE) break;
        }

        return false;
    }

    // Removes an item from the hash table
    public synchronized void remove(int item) {
        int key = hash(item);
        int i = 0;
        int new_key = (key + i) % ARR_SIZE;

        // If item not in hash table, no need to look for it again
        if (!search(item)) {
            return;
        }

        // Quadratically probe for the item
        // Dirty indices are considered to be a collision, the item may of been probed
        // farther down
        while (!cleans[new_key]) {
            if (item == table[new_key]) {
                nulls[new_key] = true;
                table[new_key] = 0;
                break;
            }
            i++;
            new_key = (new_key + i) % ARR_SIZE;
        }

        // Decrease capacity
        capacity--;
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
        return item % 8089;
    }

    // Prints the hash table in the same format as Java's own HashTable
    @Override
    public String toString() {
        String s = "{";

        int index = 0, found = 0;
        while (found <= capacity && index < ARR_SIZE) {
            if (!nulls[index]) {
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
