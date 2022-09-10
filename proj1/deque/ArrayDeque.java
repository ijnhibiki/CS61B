package deque;

public class ArrayDeque<T> {
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

    private void resize(int capacity, int indicator) {
        if (indicator == 1) {
            int first = nextFirst;
            int last = nextLast;
            T[] temparray = (T[]) new Object[capacity / 2];
            System.arraycopy(items, first + 1, temparray, 0, size - first - 1);
            System.arraycopy(items, 0, temparray, size - first - 1, last);
            T[] newarray = (T[]) new Object[capacity];
            System.arraycopy(temparray, 0, newarray, 5, size);
            items = newarray;
            nextFirst = 4;
            nextLast = 4 + size;
        }
    }


    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2, 1);
        }
        if (nextFirst == -1 && size < items.length) {
            nextFirst = items.length - 1;
        }
        items[nextFirst] = item;
        size = size + 1;
        nextFirst = nextFirst - 1;
    }
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2, 1);
        }
        if (nextLast == items.length && size < items.length) {
            nextLast = 0;
        }
        items[nextLast] = item;
        size = size + 1;
        nextLast = nextLast + 1;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int index = nextFirst + 1;
        for (int counter = 0; counter < size; counter++) {
            if (index == items.length) {
                index = 0;
            }
            System.out.print(get(index) + " ");
            index += 1;
        }
    }

    public T removeFirst() {
        int index = nextFirst + 1;
        if (index == items.length) {
            index = 0;
        }
        if (get(index) == null) {
            return null;
        }
        T firstvalue = get(index);
        items[index] = null;
        size = size - 1;
        nextFirst = nextFirst + 1;
        if (nextFirst == items.length) {
            nextFirst = 0;
        }
        return firstvalue;
    }

    public T removeLast() {
        int index = nextLast - 1;
        if (index == -1) {
            index = items.length - 1;
        }
        if (get(index) == null) {
            return null;
        }
        T lastvalue = get(index);
        items[index] = null;
        size = size - 1;
        nextLast = nextLast - 1;
        if (nextLast == -1) {
            nextLast = items.length - 1;
        }
        return lastvalue;
    }

    public T get(int index) {
        return items[index];
    }

    public boolean equals(Object o) {
        return true;
    }
}
