package deque;


import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque <T> {
    ArrayDeque<T> MaxArrayDeque;
    public MaxArrayDeque(Comparator<T> c) {
        MaxArrayDeque = new ArrayDeque<>();
        Comparator<T> current = c;

    }
    public T max() {
        if (MaxArrayDeque.isEmpty()) {
            return null;
        }
        return null;
    }
    public T max(Comparator<T> c) {
        if (MaxArrayDeque.isEmpty()) {
            return null;
        }
        return null;
    }


}
