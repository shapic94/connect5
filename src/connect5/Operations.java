package connect5;

import entity.Token;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by nebojsa.sapic on 8/9/17.
 */
public class Operations implements OperationsAbstract {

    @Override
    public Object[] init(int row, int col, int tokens, int times) {

        // Create algorithm
        Algorithm algorithm = new Algorithm();

        // Init rows
        algorithm.setRow(row);

        // Init columns
        algorithm.setCol(col);

        // Init tokens
        algorithm.setTokens(tokens);

        // Init times
        algorithm.setTimes(times);

        // Init fullColumn
        algorithm.setFullColumn(new boolean[algorithm.getCol()]);
        algorithm.setFullColumnTrue();

        // Init game
        algorithm.setGame(new int[algorithm.getRow()][algorithm.getCol()]);

        // Create Tokens
        Token black = new Token(0, false);
        Token white = new Token(0, false);

        Object[] object = new Object[3];
        object[0] = algorithm;
        object[1] = black;
        object[2] = white;

        return object;

    }

    @Override
    public void test(Algorithm algorithm, Token black, Token white) {
        // Random column
        int random;

        // Random token
        int token;

        // Check if token has already in column
        int[] columnHasToken = new int[algorithm.getCol()];


        for (int i = 0; i < algorithm.getTokens(); i++) {
            random = ThreadLocalRandom.current().nextInt(0, algorithm.getCol());
            token = ThreadLocalRandom.current().nextInt(1, Token.COUNT + 1);

            if (token == Token.BLACK) {
                black.setTurn(true);
                white.setTurn(false);
                add(algorithm, black, white);

            } else if (token == Token.WHITE) {
                black.setTurn(false);
                white.setTurn(true);
                add(algorithm, black, white);
            }
        }
    }

    @Override
    public boolean testSameColumn(int[] columnHasToken) {
        for (int i = 0; i < columnHasToken.length; i++) {
            if (columnHasToken[i] == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int[] play(Algorithm algorithm, Token black, Token white) {

        turn(algorithm, black, white);

        while(true) {
            // Check if table is full
            if (checkFullGame(algorithm)) {
                break;
            }
            add(algorithm, black, white);
        }

        return new int[0];
    }

    @Override
    public void show(Algorithm algorithm) {
        for(int i = 0; i < algorithm.getRow(); i++) {
            for(int j = 0; j < algorithm.getCol(); j++) {
                System.out.print(algorithm.getGame()[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    public void add(Algorithm algorithm, Token black, Token white) {
        while (true) {
            if (checkFullGame(algorithm)) {
                System.out.println("[Operations] Popunjeno");
                break;
            }
            int random = ThreadLocalRandom.current().nextInt(0, algorithm.getCol());
            int i = 0;

            // Check if repeated random
            if (!algorithm.getFullColumn()[random]) {
                continue;
            }
            while (true) {
                // Check if column is full
                if (algorithm.getGame()[i][random] == Token.BLACK || algorithm.getGame()[i][random] == Token.WHITE) {
                    algorithm.getFullColumn()[random] = false;
                    algorithm.setFullColumn(algorithm.getFullColumn());
                    break;
                }

                // Check if next row in column is bigger than last
                if (i + 1 >= algorithm.getRow()) {
                    addInGame(algorithm, black, white, random, i);
                    break;
                }

                // Check if next row in column is busy
                if (algorithm.getGame()[i + 1][random] == Token.BLACK || algorithm.getGame()[i + 1][random] == Token.WHITE) {
                    addInGame(algorithm, black, white, random, i);
                    break;
                }
                i++;
            }

            // Check again if repeated random because first condition in while loop
            if (!algorithm.getFullColumn()[random]) {
                continue;
            }
            break;
        }
    }

    @Override
    public void addInGame(Algorithm algorithm, Token black, Token white, int random, int i) {
        if (black.getTurn()) {
            algorithm.getGame()[i][random] = Token.BLACK;
            black.setTurn(false);
            white.setTurn(true);
        } else {
            algorithm.getGame()[i][random] = Token.WHITE;
            black.setTurn(true);
            white.setTurn(false);
        }
        algorithm.setGame(algorithm.getGame());
    }

    @Override
    public void check(Algorithm algorithm) {

    }

    @Override
    public boolean checkFullGame(Algorithm algorithm) {
        for (int i = 0; i < algorithm.getFullColumn().length; i++) {
            if (algorithm.getFullColumn()[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void turn(Algorithm algorithm, Token black, Token white) {
        for (int i = 0; i < algorithm.getRow(); i++) {
            for (int j = 0; j < algorithm.getCol(); j++) {
                if (algorithm.getGame()[i][j] == Token.BLACK) {
                    black.setFields(black.getFields() + 1);
                } else if (algorithm.getGame()[i][j] == Token.WHITE) {
                    white.setFields(white.getFields() + 1);
                }
            }
        }

        if (black.getFields() < white.getFields()) {
            System.out.println("[Operations] Black token has first turn.");
            black.setTurn(true);
        } else {
            System.out.println("[Operations] White token has first turn.");
            white.setTurn(true);
        }
    }
}
