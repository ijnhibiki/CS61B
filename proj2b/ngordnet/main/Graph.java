package ngordnet.main;

import java.util.*;

public class Graph {

    private int edge;
    private int vertices;
    private Map<Integer, List<Integer>> brackets;
    private LinkedList<Integer> neighbor;
    //Adjacency Matrix.
    public Graph() {
        this.brackets = new HashMap<>();
        this.edge = 0;
        this.vertices = 0;
    }

    public void addEdge(Integer parent, Integer child) {
        if (brackets.containsKey(parent)) {
            brackets.get(parent).add(child);
            edge += 1;
        }
    }
    public void addNode(Integer node) {
        if (!brackets.containsKey(node)) {
            neighbor = new LinkedList<>();
            brackets.put(node, neighbor);
            vertices += 1;
        }
    }

    public int getEdge() {
        return edge;
    }

    public int getVertex() {
        return vertices;
    }

    public List<Integer> getNodes() {
        return new LinkedList<>(brackets.keySet());
    }

    public List<Integer> neighbors(int vertex) {
        if (brackets.containsKey(vertex)) {
            return brackets.get(vertex);
        }
        return null;
    }

    public Set<Integer> depthFirstTraversal(Graph graph, Integer root) {
        Set<Integer> visited = new LinkedHashSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Integer vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Integer index : graph.neighbors(vertex)) {
                    stack.push(index);
                }
            }
        }
        return visited;
    }


}
