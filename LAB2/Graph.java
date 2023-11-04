// Java program to print BFS traversal from a given source
// vertex. BFS(int s) traverses vertices reachable from s.

import java.util.*;

// This class represents a directed graph using adjacency
// list representation
public class Graph<T> {

    // Adjacency Lists
    private final Map<T, Set<T>> adj;

    // Constructor
    public Graph(Set<T> v) {
        // List of vertices
        adj = new LinkedHashMap<>();
        for (T t : v) {
            adj.put(t, new LinkedHashSet<>());
        }
    }

    // Function to add an edge into the graph
    public void addEdge(T v, T w) {
        adj.get(v).add(w);
    }

    // prints BFS traversal from a given source s
    public Map<T, Boolean> BFS(T t) {
        // Mark all the vertices as not visited(By default
        // set as false)
        Map<T, Boolean> visited = new LinkedHashMap<>();

        // initialize all vertices as not visited
        for (T n : adj.keySet()) {
            visited.put(n, false);
        }

        // Create a queue for BFS
        LinkedList<T> queue = new LinkedList<>();

        // Mark the current node as visited and enqueue it
        visited.put(t, true);
        queue.add(t);

        while (!queue.isEmpty()) {

            // Dequeue a vertex from queue and print it
            t = queue.poll();
            //System.out.print(t + " ");

            // Get all adjacent vertices of the dequeued
            // vertex s.
            // If an adjacent has not been visited,
            // then mark it visited and enqueue it
            for (T n : adj.get(t)) {
                if (!visited.get(n)) {
                    visited.put(n, true);
                    queue.add(n);
                }
            }
        }

        return visited;
    }

    public static void main(String[] args) {
        Set<String> v = Set.of("%", "S", "A", "B", "a", "b");
        Graph<String> g = new Graph<>(v);
        g.addEdge("%", "S");
        g.addEdge("S", "A");
        g.addEdge("A", "B");
        g.addEdge("B", "a");
        g.addEdge("B", "b");

        Map<String, Boolean> visited = g.BFS("B");
        for (String n : visited.keySet()) {
            System.out.println(n + " " + visited.get(n));
        }
    }
}