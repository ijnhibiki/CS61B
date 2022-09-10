package deque;

public class ArrayDeque<T> {
    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int head = 7;


    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 7;
        nextLast = 0;
    }

    private void resize(int capacity, int indicator) {
        if (indicator == 1) {
            T[] newarray = (T[]) new Object[capacity];
            System.arraycopy(items, 0, newarray, 0, size);
            items = newarray;
            nextFirst = capacity - 1;
            nextLast = size;
            head = size;
        }
        if (indicator == 2) {
            T[] newarray = (T[]) new Object[capacity];
            System.arraycopy(items, 0, newarray, 0, capacity);
            items = newarray;
        }
    }


    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2, 1);
        }
        if (nextFirst == -1 && size < items.length || get(nextFirst) != null) {
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
            nextLast = head;
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
            index = head;
        }
        if (get(index) == null && size == 0) {
            return null;
        }
        nextFirst = nextFirst + 1;
        if (size == 8) {
            index = 0;
            nextFirst = index;
        }
        if (get(index) == null && size != 0) {
            index = size / 2;
            nextFirst = index;
        }
        T firstvalue = get(index);
        items[index] = null;
        if (nextFirst == items.length) {
            nextFirst = head;
        }
        size = size - 1;
        if (size / (double) items.length < 0.25 && size >= 16) {
            resize(items.length / 2, 2);
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
        if (size / (double) items.length < 0.25 && size >= 16) {
            resize(items.length / 2, 2);
        }
        return lastvalue;
    }

    public T get(int index) {
        return items[index];
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof ArrayDeque<?>)) {
            return false;
        }  else if (size != ((ArrayDeque<?>) o).size) {
            return false;
        } else if (o == this) {
            return true;
        }
        ArrayDeque<?> p2 = ((ArrayDeque<?>) o);
        for (int counter = 0; counter < items.length; counter += 1) {
            if (items[counter] != null && !(items[counter].equals(p2.items[counter]))) {
                return false;
            }
        }
        return true;
    }
}
