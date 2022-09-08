package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> test1 = new AListNoResizing<>();
        BuggyAList<Integer> test2 = new BuggyAList<>();
        for (int i = 4; i <= 6; i +=1) {
            test1.addLast(i);
            test2.addLast(i);
        }
        assertEquals(test1.size(), test2.size());
        for (int i = 1; i <= 3; i += 1){
            assertEquals(test1.removeLast(), test2.removeLast());
        }
    }


    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> M = new BuggyAList<>();
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
                assertEquals(L.getLast(), M.getLast());
                assertEquals(L.removeLast(), M.removeLast());
            }
        }
    }
}
