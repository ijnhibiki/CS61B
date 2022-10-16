package hashmap;

import java.util.*;


/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Haobo Chen
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private double maxLoad;
    private int size;
    private Set<K> set;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(INITIAL_CAPACITY, LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.buckets = createTable(initialSize);
        this.maxLoad = maxLoad;
        this.set = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        if (tableSize <= 0) {
            throw new IllegalArgumentException();
        }
        Collection<Node>[] table;
        table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i ++) {
            table[i] = createBucket();
        }
        return table;
    }
    private int index_convertor(K key, Collection<Node>[] table) {
        int hash_index = key.hashCode();
        return Math.floorMod(hash_index, table.length);
    }



    public void clear() {
        buckets = createTable(INITIAL_CAPACITY);
        size = 0;
    }

    public int size() {
        return size;
    }

    public V get(K key) {
        Node result = node_finder(key);
        if (result != null) {
            return result.value;
        }
        return null;
    }

    private Node node_finder(K key) {
        int node_index = index_convertor(key, buckets);
        Collection<Node> result_busket = buckets[node_index];
        for (Node result : result_busket) {
            if (result.key.hashCode() == key.hashCode()) {
                return result;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        int node_index = index_convertor(key, buckets);
        Node original_node = node_finder(key);
        if (containsKey(key) && original_node != null) {
            buckets[node_index].remove(original_node);
            size = size - 1;
            return original_node.value;
        }
        return null;
    }

    public Set<K> keySet() {
        return set;
    }

    public V remove(K key, V value) {
        if (get(key) == value) {
            remove(key);
        }
        return null;
    }

    public void put(K key, V value) {
        set.add(key);
        int node_index = index_convertor(key, buckets);
        Node add_node = createNode(key, value);
        Node original_node = node_finder(key);
        if (containsKey(key)) {
            if ((original_node) != null) {
                original_node.value = value;
            }
        } else {
            buckets[node_index].add(add_node);
            size = size + 1;
        }
        if((double)size / buckets.length > maxLoad) {
            resize(buckets.length * 2);
        }

    }




    public Iterator<K> iterator() {
        return new Hashmap_iterator();
    }
    private class Hashmap_iterator implements Iterator<K> {
        private final Iterator<Node> nodeIterator = new HashMapNodeIterator();

        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        public K next() {
            return nodeIterator.next().key;
        }
    }

    private class HashMapNodeIterator implements Iterator<Node> {
        private final Iterator<Collection<Node>> bucketsIterator = Arrays.stream(buckets).iterator();
        private Iterator<Node> currentBucketIterator;
        private int nodesLeft = size;

        public boolean hasNext() {
            return nodesLeft > 0;
        }

        public Node next() {
            if (currentBucketIterator == null || !currentBucketIterator.hasNext()) {
                Collection<Node> currentBucket = bucketsIterator.next();
                while (currentBucket.size() == 0) {
                    currentBucket = bucketsIterator.next();
                }
                currentBucketIterator = currentBucket.iterator();
            }
            nodesLeft -= 1;
            return currentBucketIterator.next();
        }
    }


    private void resize (int capacity) {
        Collection<Node>[] updated_bucket = createTable(capacity);
        Iterator<K> key_iterator = set.iterator();
        while (key_iterator.hasNext()) {
            K key = key_iterator.next();
            int bucketIndex = index_convertor(key, updated_bucket);
            updated_bucket[bucketIndex].add(createNode(key, get(key)));
        }
        buckets = updated_bucket;


    }


    // Your code won't compile until you do so!

}
