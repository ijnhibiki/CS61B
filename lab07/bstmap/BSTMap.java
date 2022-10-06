package bstmap;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left, right;
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private Set<K> set;
    private BSTNode root;
    private int size;
    public BSTMap() {
        root = null;
        size = 0;
        set = new HashSet<>();
    }

    public void clear() {
        root = null;
        size = 0;
    }
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return containsKey(root, key);
    }

    private boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return containsKey(node.left, key);
        } else {
            return containsKey(node.right, key);
        }
    }


    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode node, K key) {
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
        return size(root);
    }

    private int size(BSTNode node) {
        if (node == null) {
            return 0;
        } else {
            return size;
        }
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }

        root = put(root, key, value);
        set.add(key);
        size = size + 1;

    }

    private BSTNode put(BSTNode node, K key, V value) {
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
        return set;
    }

    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        V removed = get(key);
        root = remove(root, key);
        size = size - 1;
        return removed;
    }
    private BSTNode remove(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int comp = key.compareTo(node.key);
        if (comp < 0) {
            node.left  = remove(node.left,  key);
        } else if (comp > 0) {
            node.right = remove(node.right, key);
        } else {
            if (node.right == null) {
                return node.left;
            }
            if (node.left  == null) {
                return node.right;
            }
            BSTNode newnode = node;
            node = maxfinder(newnode.left);
            node.left = deleteMax(newnode.left);
            node.right = newnode.right;

        }
        return node;
    }

    private BSTNode deleteMax(BSTNode node) {
        if (node.right == null) {
            return node.left;
        }
        node.right = deleteMax(node.right);
        size = 1 + size(node.left) + size(node.right);
        return node;
    }


    private BSTNode maxfinder(BSTNode node) {
        if (size == 0) {
            throw new IllegalArgumentException("calls min() with empty symbol table");
        }
        if (node.right == null) {
            return node;
        } else {
            return maxfinder(node.right);
        }
    }


    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
    private class BSTMapIterator implements Iterator<K> {
        private int wizPos;
        private BSTMapIterator() {
            wizPos = 0;
        }
        public boolean hasNext() {
            return wizPos < size;
        }
        public K next() {
            return set.iterator().next();
        }
    }
    public void printInOrder() {
        if (root == null) {
            System.out.println("None");
        } else {
            printInOrder(root);
        }
    }
    private void printInOrder(BSTNode node) {
        if (node.right == null && node.left == null) {
            System.out.println(" " + node.key.toString() + " ");
        } else if (node.right == null && node.left != null) {
            printInOrder(node.left);
            System.out.println(" " + node.key.toString() + " ");
        } else if (node.right != null && node.left == null) {
            printInOrder(node.right);
            System.out.println(" " + node.key.toString() + " ");
        } else {
            printInOrder(node.left);
            System.out.println(" " + node.key.toString() + " ");
            printInOrder(node.right);
        }
    }



}
