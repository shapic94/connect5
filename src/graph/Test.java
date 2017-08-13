package graph;

public class Test {

    public static void main(String[] args) {
        Graph G = new Graph();
        G.addEdge("A", "B");
        G.addEdge("A", "C");
        G.addEdge("C", "D");
        G.addEdge("D", "E");
        G.addEdge("D", "G");
        G.addEdge("E", "G");
        G.addEdge("R", "G");
        G.addVertex("H");

        // print graph
        System.out.println(G);
    }
}
