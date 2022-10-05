package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap <K extends Comparable<K>, V>implements Map61B<K, V> {

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left, right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private BSTNode root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }
    public void clear() {
        root = null;
        size = 0;
    }
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return Keyfinder(root, key);
    }
    private boolean Keyfinder(BSTNode node, K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (node == null) {
            return false;
        }
        int comp = key.compareTo(node.key);
        if (comp < 0) {
            get(node.left, key);
        }
        if (comp > 0) {
            get(node.right, key);
        }
        return true;
    }

    public V get(K key) {
        return get(root, key);
    }

    private V get (BSTNode node, K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (node == null) {
            return null;
        }
        int comp = key.compareTo(node.key);
        if (comp < 0) {
            return get(node.left, key);
        }
        if (comp > 0) {
            return get(node.right, key);
        }
        return node.value;
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }

        root = put(root, key, value);
        size = size + 1;

    }

    private BSTNode put (BSTNode node, K key, V value) {
        if (node == null) {
            return new BSTNode(key, value);
        }
        int comp = key.compareTo(node.key);
        if (comp < 0) {
            node.left = put(node.left, key, value);
        }
        if (comp > 0) {
            node.right = put(node.right, key, value);
        }
        if (comp == 0) {
            node.value = value;
            size = size - 1;
        }
        return node;
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();

    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
    private class BSTMapIterator implements Iterator<K> {
        public boolean hasNext() {
            return true;
        }
        public K next() {

            return null;
        }
    }
}
