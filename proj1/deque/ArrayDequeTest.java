package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class ArrayDequeTest {

    @Test
    public void addIsEmptySizeTest() {
        ArrayDeque<String> lld1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();
    }

    @Test
    public void ArraycircularTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());

       for (int i = 0; i < 7; i ++) {
           lld1.addFirst(i);
       }
       assertEquals(lld1.size(), 7);
       System.out.println("Printing out deque: ");
       lld1.printDeque();

       ArrayDeque<Integer> lld2 = new ArrayDeque<>();

       assertTrue("A newly initialized LLDeque should be empty", lld2.isEmpty());
       for (int i = 0; i < 7; i ++) {
            lld2.addLast(i);
        }
       assertEquals(lld2.size(), 7);
    }

    @Test
    public void addRemoveTest() {


        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());
        lld1.addLast(20);
        lld1.addLast(30);

        lld1.removeFirst();
        // should be empty
        assertFalse("lld1 should be empty after removal", lld1.isEmpty());

    }
    @Test
    public void Resize1Test() {


        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());
        for (int i = 0; i < 10; i++) {
            lld1.addFirst(i);
        }
        assertEquals(lld1.size(), 10);
    }
    @Test
    public void RemoveFirst() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());
        for (int i = 0; i < 7; i++) {
            lld1.addFirst(i);
        }
        for (int i = 0; i < 7; i++) {
            lld1.removeFirst();
        }
        assertEquals(lld1.size(), 0);
    }

    @Test
    public void RemoveLast() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());
        for (int i = 0; i < 7; i++) {
            lld1.addFirst(i);
        }
        for (int i = 0; i < 7; i++) {
            lld1.removeLast();
        }
        assertEquals(lld1.size(), 0);
    }
    @Test
    public void addgetTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());
        for (int i = 0; i < 25; i++) {
            lld1.addFirst(i);
        }
        assertEquals(lld1.get(0), (Object) 7);
    }

    @Test
    public void bigLLDequeTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 10000; i++) {
            lld1.addFirst(i);
        }

        for (double i = 9999; i >=0; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
            }
    }
    @Test
    public void bigLLDeque2Test() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 10000; i++) {
            lld1.addLast(i);
        }

        for (double i = 9999; i >= 0; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }
    @Test
    public void printDeque() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 50; i++) {
            lld1.addLast(i);
        }
        lld1.addLast(344);
        lld1.printDeque();
    }

    @Test
    public void Equal2Test() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 5000; i++) {
            lld1.addLast(i);
        }

        ArrayDeque<Integer> lld2 = new ArrayDeque<>();
        for (int i = 0; i < 5000; i++) {
            lld2.addLast(i);
        }
        assertEquals(lld1.equals(lld2), true);
    }
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        ArrayDeque<Integer> M = new ArrayDeque<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                M.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                assertEquals(size, M.size());
            } else if (operationNumber == 2 && L.size()> 0) {
                assertEquals(L.removeLast(), M.removeLast());
            }
        }
    }
    @Test
    public void iteratorTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 500; i++) {
            lld1.addLast(i);
        }
        Iterator<Integer> seer
                = lld1.iterator();
        while (seer.hasNext()) {
            System.out.println(seer.next());
        }

    }
}
