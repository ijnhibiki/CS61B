package ngordnet.main;

import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    @Test
    public void TestSimple() {
        Graph test1 = new Graph();
        for (int i = 0; i < 10; i ++) {
            test1.addNode(i);
        }
        for (int i = 1; i < 10; i ++) {
            test1.addEdge(i-1, i);
        }
        assertEquals(10, test1.getVertex());
        assertEquals(9, test1.getEdge());
    }


}
