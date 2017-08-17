package graph;

import global.Methods;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
//        String test = "0";
//        System.out.println(Methods.isParentVertex("0"));
//        System.out.println(test1);
//        Graph G = new Graph();
//        G.addEdge("A", "B");
//        G.addEdge("A", "C");
//        G.addEdge("C", "D");
//        G.addEdge("D", "E");
//        G.addEdge("D", "G");
//        G.addEdge("E", "G");
//        G.addEdge("R", "G");
//        G.addVertex("H");
//
//        // print graph
//        System.out.println(G);
//
//        System.out.println(G.getVertices().toString());
//
//        String[] test = Methods.parseVertices(G.getVertices().toString());
//        for (int i = 0; i < test.length; i++) {
//            System.out.println(test[i]);
//        }
        String test = "0.1.1.0.0.0.0.0";
        System.out.println(numberOfChildren(test));

    }

    public static int numberOfChildren(String id) {
        int count = 0;
        String[] parseValue = id.split("\\.");
        for (int i = parseValue.length - 1; i >= 0; i--) {
            if (parseValue[i].equals("0")) {
                count++;
                continue;
            } else {
                break;
            }
        }
        return (int) Math.pow(3, Math.floor(count / 2.0));
    }
}
