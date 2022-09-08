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
        size = 0;
    }




    public void addFirst(T item) {
        sentinel.next = new Deque(sentinel.prev, item, sentinel.next);
        size = size +1;
    }

    public void addLast(T item){
        return;
    }

    public boolean isEmpty(){
        return true;
    }


    public T getFirst() {
        return sentinel.next.item;
    }

    public void printDeque(){
        return;
    }

    public T removeFirst(){
        return null;
    }

    public T removeLast(){
        return null;
    }

    public T get(int index){
        return null;
    }
    public int size() {
        return size;
    }


    public static void main(String[] args) {
        /* Creates a list of one integer, namely 10 */
        LinkedListDeque <Integer> L = new LinkedListDeque<Integer>();
        L.addFirst(20);
        L.addFirst(30);
        System.out.println(L.size());
    }
}