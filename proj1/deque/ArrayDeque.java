package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;


    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    private void resize(int capacity) {
        int taillength = size - nextLast;
        T[] newarray = (T[]) new Object[capacity];
        System.arraycopy(items, 0, newarray, 0, nextLast);
        System.arraycopy(items, nextFirst + 1, newarray, capacity - taillength, taillength);
        items = newarray;
        nextFirst = capacity - (size - nextLast) - 1;
    }


    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        if (nextFirst == -1) {
            nextFirst = items.length - 1;
        }
        items[nextFirst] = item;
        size = size + 1;
        nextFirst = nextFirst - 1;
    }
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        if (nextLast == items.length) {
            nextLast = 0;
        }
        items[nextLast] = item;
        size = size + 1;
        nextLast = nextLast + 1;
    }


    public int size() {
        return size;
    }

    public void printDeque() {
        for (int counter = 0; counter < size; counter++) {
            System.out.print(get(counter) + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (size / (double) items.length < 0.25 && size > 8) {
            resize(items.length / 2);
        }
        T firstvalue = get(0);
        nextFirst = (nextFirst + 1) % items.length;
        items[nextFirst] = null;
        size = size - 1;
        return firstvalue;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (size / (double) items.length < 0.25 && size > 8) {
            resize(items.length / 2);
        }
        T lastvalue = get(size - 1);
        nextLast = (nextLast - 1 + items.length) % items.length;
        items[nextLast] = null;
        size = size - 1;

        return lastvalue;
    }

    public T get(int index) {
        int innerindex = nextFirst + 1 + index;
        if (innerindex >= items.length) {
            innerindex = innerindex - items.length;
        }
        return items[innerindex];
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
        return new ArrayDequeIterator();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;
        private ArrayDequeIterator() {
            wizPos = 0;
            if (wizPos == items.length) {
                wizPos = 0;
            }
        }
        public boolean hasNext() {
            return wizPos < size;
        }
        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }
}
