package deque;

import org.junit.Test;
import static org.junit.Assert.*;

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
}
