package deque;


public class LinkedListDeque<T> {
    private class Deque {
        public Deque next;
        public Deque prev;
        public T item;

        public Deque(Deque p, T i, Deque n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Deque sentinel;

    private int size;



    public LinkedListDeque() {
        sentinel = new Deque(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    public void addFirst(T item) {
        sentinel.next = new Deque(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size = size +1;
    }

    public void addLast(T item){
        sentinel.prev.next = new Deque(sentinel.prev, item, sentinel);
        sentinel.prev = sentinel.prev.next;
        size = size + 1;
    }

    public boolean isEmpty(){
        if (sentinel.next.item == null) {
            return true;
        }
        return false;
    }


    public T getFirst() {
        return sentinel.item;
    }

    public void printDeque(){
        for (int counter = 0; counter < size; counter +=1) {
            System.out.print(sentinel.next.item + " ");
            sentinel.next = sentinel.next.next;
        }
        System.out.print("\n");
    }

    public T removeFirst(){
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

    public T removeLast(){
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

    public T get(int index){
        if (index >= size){
            return null;
        }
        while (index > 0) {
            sentinel.next = sentinel.next.next;
            index -= 1;
        }
        return sentinel.next.item;
    }
    public int size() {
        return size;
    }
}