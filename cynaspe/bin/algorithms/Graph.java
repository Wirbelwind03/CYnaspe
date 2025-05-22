import java.util.*;

/**
 * Represents a graph using adjacency lists.
 */
public class Graph {
    private final Map<Node, List<Node>> adjacencyList = new HashMap<>();
    private final boolean isDirected;

    public Graph(boolean isDirected) {
        this.isDirected = isDirected;
    }

    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node source, Node destination) {
        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());
        adjacencyList.get(source).add(destination);

        if (!isDirected) {
            adjacencyList.get(destination).add(source);
        }
    }

    public List<Node> getNeighbors(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public Set<Node> getAllNodes() {
        return adjacencyList.keySet();
    }

    public boolean containsNode(Node node) {
        return adjacencyList.containsKey(node);
    }
}
