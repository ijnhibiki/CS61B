package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

public class CommitGraph implements Serializable {
    private int edge;
    private int vertices;
    private Map<String, HashSet<String>> brackets;
    private HashSet<String> parents;


    public CommitGraph() {
        this.brackets = new HashMap<>();
        this.edge = 0;
        this.vertices = 0;
    }

    public void addEdge(String parent, String child) {
        if (brackets.containsKey(child)) {
            brackets.get(child).add(parent);
            edge += 1;
        }
    }

    public void addNode(String node) {
        if (!brackets.containsKey(node)) {
            parents = new HashSet<>();
            brackets.put(node, parents);
            vertices += 1;
        }
    }

    public int getEdge() {
        return edge;
    }

    public int getVertices() {
        return vertices;
    }

    public HashSet<String> getParents(String node) {
        if (brackets.containsKey(node)) {
            return brackets.get(node);
        }
        return null;
    }

    public String findLCA(String node1, String node2) {
        LinkedHashSet<String> ancestors1 = new LinkedHashSet<>();
        LinkedHashSet<String> ancestors2 = new LinkedHashSet<>();
        ancestors1.add(node1);
        ancestors2.add(node2);

        // Traverse up from node1 and add all ancestors to the set
        traverseUp(node1, ancestors1);

        // Traverse up from node2 and find the first ancestor that is also in ancestors1
        while (!ancestors1.contains(node2)) {
            traverseUp(node2, ancestors2);
            for (String ancestor : ancestors2) {
                if (ancestors1.contains(ancestor)) {
                    return ancestor;
                }
            }
            ancestors2.clear();
        }

        return node2; // If no common ancestor is found, return node2
    }

    private void traverseUp(String node, LinkedHashSet<String> ancestors) {
        if (!brackets.containsKey(node)) {
            return;
        }
        for (String parent : brackets.get(node)) {
            ancestors.add(parent);
            traverseUp(parent, ancestors);
        }
    }

}
