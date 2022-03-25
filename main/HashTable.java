class HashTable {
    public static final int ARR_SIZE = 128;

    private int[] table = new int[ARR_SIZE];
    private boolean[] nulls = new boolean[ARR_SIZE];
    private boolean[] cleans = new boolean[ARR_SIZE];
    private int capacity;

    public static void main(String[] args) {

    }

    public HashTable() {
        for (int i = 0; i < ARR_SIZE; i++) {
            table[i] = 0;
            nulls[i] = true;
            cleans[i] = true;
        }
        capacity = 0;
    }

    public void put(int item) {
        if (capacity == ARR_SIZE)
            return;
        int key = hash(item);

        // System.out.println("Item: " + item + ", key: " + key);

        if (nulls[key]) {
            // System.out.println(key + " is null index (" + item + ")");
            table[key] = item;
            nulls[key] = false;
            cleans[key] = false;
        } else {
            // System.out.println(key + " is not null index (" + item + ")");
            int i = 1;
            while (!nulls[key + (i * i)]) {
                // System.out.println("p: " + (key + (i * i)) + " is not null index (" + item +
                // ")");
                i++;
            }
            // System.out.println("p: " + (key + (i * i)) + " is null index (" + item +
            // ")");
            table[key + (i * i)] = item;
            nulls[key + (i * i)] = false;
            cleans[key + (i * i)] = false;
        }

        capacity++;
    }

    public boolean search(int item) {
        int key = hash(item);
        int i = 0;

        while (!cleans[key + (i * i)]) {
            if (item == table[key + (i * i)] && !nulls[key + (i * i)]) {
                return true;
            }
            i++;
        }

        return false;
    }

    public void remove(int item) {
        int key = hash(item);
        int i = 0;

        if (!search(item)) {
            return;
        }

        while (!cleans[key + (i * i)]) {
            if (item == table[key + (i * i)]) {
                nulls[key + (i * i)] = true;
                table[key + (i * i)] = 0;
            }
            i++;
        }
        capacity--;
    }

    public int getSize() {
        return capacity;
    }

    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    // Need to make a better hash function
    private int hash(int item) {
        return item % 128;
    }

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