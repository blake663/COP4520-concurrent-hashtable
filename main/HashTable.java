public class HashTable {
    public static final int ARR_SIZE = 64708; // The maximum capacity of a HashTable
    private Integer[] table = new Integer[ARR_SIZE]; // Where values are stored
    private boolean[] cleans = new boolean[ARR_SIZE]; // Where clean indices are stored
    private int capacity;

    public HashTable() {
        // Generate arrays, each with default values. Set capacity to 0
        for (int i = 0; i < ARR_SIZE; i++) {
            table[i] = null;
            cleans[i] = true;
        }
        capacity = 0;
    }

    // Add an item to the hash table
    public boolean put(int item) {
        // If capacity if full, do not add
        if (capacity >= ARR_SIZE)
            return false;

        // Get key
        long key = hash(item);

        // Check if first try addition
        if (table[(int) key] == null) {
            table[(int) key] = item;
            cleans[(int) key] = false;
        }
        // Otherwise, quadratically probe for empty indices
        else {
            long i = 1;
            long new_key = (key + i) % ARR_SIZE;
            try {
                while (table[(int) new_key] != null) {
                    i++;
                    // if (item == 64708) System.out.println(String.format("i: %d, key: %d, new key:
                    // %d, item: %d", i, key, new_key, item));
                    new_key = (key + i) % ARR_SIZE;
                    // if (item == 64708) System.out.println(String.format("i: %d, key: %d, new key:
                    // %d, item: %d", i, key, new_key, item));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(String.format("i: %d, key: %d, new key: %d, item: %d", i, key, new_key, item));
            }
            table[(int) new_key] = item;
            cleans[(int) new_key] = false;
        }

        // Increase capacity
        capacity++;
        return true;
    }

    // Search the hash table for a specific item, return true if found, else false
    public boolean search(int item) {
        int key = hash(item);
        int i = 0;
        int new_key = (key + i) % ARR_SIZE;
        // Quadratically probe for the item
        // Dirty indices are considered to be a collision, the item may of been probed
        // farther down
        while (!cleans[new_key]) {
            if (table[new_key] == null) {
            } else if (item == table[new_key]) {
                return true;
            }
            i++;
            new_key = (key + i) % ARR_SIZE;
            if (i >= ARR_SIZE)
                break;
        }

        return false;
    }

    // Removes an item from the hash table
    public void remove(int item) {
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
                table[new_key] = null;
                break;
            }
            i++;
            new_key = (key + i) % ARR_SIZE;
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
        return item % 64708;
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