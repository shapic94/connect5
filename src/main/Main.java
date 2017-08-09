package main;

import connect5.Algorithm;
import connect5.Operations;

/**
 * Created by nebojsa.sapic on 8/8/17.
 */
public class Main {

    public static void main(String [] args) {
        System.out.println("Welcome to connect5 game");
        int row = 10;
        int col = 10;
        int[][] game = new int[row][col];
        Algorithm algorithm = new Algorithm(10, 10, game);
    }
}