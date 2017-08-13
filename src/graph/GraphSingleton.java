package graph;

public class GraphSingleton extends Graph {
    private static GraphSingleton myInstance;

    private GraphSingleton() {

    }

    public static GraphSingleton getInstance() {
        if (myInstance == null) {
            myInstance = new GraphSingleton();
        }

        return myInstance;
    }
}