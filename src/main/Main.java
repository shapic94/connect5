package main;

import connect5.Algorithm;
import connect5.Operations;
import entity.Token;

/**
 * Created by nebojsa.sapic on 8/8/17.
 *
 * Black = 1
 * White = 2
 */
public class Main {

    public static void main(String [] args) {
        System.out.println("Welcome to connect5 game");

        // Rows
        int row = 10;

        // Columns
        int col = 10;

        // Tokens
        int tokens = 5;

        // Times
        int times = 10;

        // Initializing
        Object[] object = new Operations().init(row, col, tokens, times);

        // Cast Algorithm from object
        Algorithm algorithm = (Algorithm) object[0];

        // Cast Tokens from object
        Token black = (Token) object[1];
        Token white = (Token) object[2];

        // Show started game
        System.out.println("[Main] Empty Game");
        algorithm.show(algorithm);

        // Start test
        System.out.println("[Main] Testing...");
        algorithm.test(algorithm, black, white);

        // Show tested game
        System.out.println("[Main] Tested Game");
        algorithm.show(algorithm);

        // Play game
        System.out.println("[Main] Play game");
        algorithm.play(algorithm, black, white);
        algorithm.show(algorithm);

    }
}