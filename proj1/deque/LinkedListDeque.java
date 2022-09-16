package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        private Node next;
        private Node prev;
        private T item;

        private Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Node sentinel;

    private int size;



    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    public void addFirst(T item) {
        sentinel.next = new Node(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size = size + 1;
    }

    public void addLast(T item) {
        sentinel.prev.next = new Node(sentinel.prev, item, sentinel);
        sentinel.prev = sentinel.prev.next;
        size = size + 1;
    }

    public void printDeque() {
        for (int counter = 0; counter < size; counter += 1) {
            System.out.print(get(counter) + " ");
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T first = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev.prev = null;
        sentinel.next.prev.next = null;
        sentinel.next.prev = sentinel;
        size = size - 1;
        return first;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T last = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next.next = null;
        sentinel.prev.next.prev = null;
        sentinel.prev.next = sentinel;
        size = size - 1;
        return last;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }
    public int size() {
        return size;
    }
    public T getRecursive(int index) {
        Node p = sentinel.next;
        return getRecursiveHelper(p, index);
    }
    private T getRecursiveHelper(Node list, int index) {
        if (index == 0) {
            return list.item;
        }
        return getRecursiveHelper(list.next, index - 1);

    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof Deque)) {
            return false;
        } else if (o == this) {
            return true;
        }
        Deque<T> p = (Deque<T>) o;
        if (p.size() != this.size) {
            return false;
        }
        for (int counter = 0; counter < size; counter++) {
            if (!(p.get(counter).equals(this.get(counter)))) {
                return false;
            }
        }
        return true;
    }
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }
    private class LinkedListIterator implements Iterator<T> {
        private int wizPos;
        private LinkedListIterator() {
            wizPos = 0;
        }
        public boolean hasNext() {
            return wizPos < size;
        }
        public T next() {
            T returnTtem = get(wizPos);
            wizPos += 1;
            return returnTtem;
        }
    }
}
