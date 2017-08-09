package main;

import connect5.Algorithm;
import connect5.Operations;

/**
 * Created by nebojsa.sapic on 8/8/17.
 *
 * Black = 1
 * White = 2
 */
public class Main {

    public static void main(String [] args) {
        System.out.println("Welcome to connect5 game");
        int row = 10;
        int col = 15;
        int[][] game = new int[row][col];
        game[row - 1][2] = 2;
        game[row - 1][7] = 1;
        game[row - 1][14] = 2;

        Algorithm algorithm = new Algorithm(row, col, game, new boolean[col]);
    }
}