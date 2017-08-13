package connect5;

import entity.Token;
import gui.Component;
import storage.Storage;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by nebojsa.sapic on 8/9/17.
 */
public class Operations implements OperationsAbstract {

    @Override
    public Object[] init(String player1, String player2, int row, int col, int tokens, int times) {

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
        Token black = new Token(player1, 0, false, false, 0);
        Token white = new Token(player2, 0, false, false, 0);

        Object[] object = new Object[3];
        object[0] = algorithm;
        object[1] = black;
        object[2] = white;

        test(algorithm, black, white);

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


        for (int i = 0; i < algorithm.getTimes(); i++) {

            // Start test
            System.out.println("[Operations][test()] Testing...");
            Component.getInstance().getResultL().setText("Testing...");

            for (int j = 0; j < algorithm.getTokens(); j++) {
                random = ThreadLocalRandom.current().nextInt(0, algorithm.getCol());
                token = ThreadLocalRandom.current().nextInt(1, Storage.COUNT + 1);

                // Check if same TOKEN two times
                if (token == Storage.BLACK && !black.getTurn() && j != 0) {
                    token = Storage.WHITE;
                } else if (token == Storage.WHITE && !white.getTurn() && j != 0) {
                    token = Storage.BLACK;
                }

                if (token == Storage.BLACK) {
                    black.setTurn(true);
                    white.setTurn(false);
                    add(algorithm, black, white);
                } else if (token == Storage.WHITE) {
                    black.setTurn(false);
                    white.setTurn(true);
                    add(algorithm, black, white);
                }
            }

            // Show tested game
            System.out.println("[Operations][test()] Tested Game");

            // Play game
            System.out.println("[Operations][test()] Play game");
            algorithm.play(algorithm, black, white);
            algorithm.show(algorithm);

            // Winner
            if (black.isWinner()) {
                System.out.println("[Operations][test()] Cestitamo, " + black.getName() + ", Row: " + algorithm.getRowWinner() + ", Column: " + algorithm.getColWinner());
            } else if (white.isWinner()) {
                System.out.println("[Operations][test()] Cestitamo, " + white.getName() + ", Row: " + algorithm.getRowWinner() + ", Column: " + algorithm.getColWinner());
            }

            reset(algorithm, black, white);
        }

        // Global winner
        System.out.println(black.getNumberOfWins());
        System.out.println(white.getNumberOfWins());
        System.out.println("---------------------------------------------------------------------------------");

        // Set Names, number of wins and percent
        Component.getInstance().getPlayer1NameL().setText(black.getName());
        Component.getInstance().getPlayer1ScoreL().setText(black.getNumberOfWins() + " pobeda ( " + ((100 * black.getNumberOfWins()) / algorithm.getTimes()) + "% )");
        Component.getInstance().getPlayer2NameL().setText(white.getName());
        Component.getInstance().getPlayer2ScoreL().setText(white.getNumberOfWins() + " pobeda ( " + ((100 * white.getNumberOfWins()) / algorithm.getTimes()) + "% )");

        if (black.getNumberOfWins() == white.getNumberOfWins()) {
            Component.getInstance().getResultL().setText("Ovo je neverovatno! Imate isti broj pobeda!");
        } else if (black.getNumberOfWins() > white.getNumberOfWins()) {
            Component.getInstance().getResultL().setText("Cestitamo, " + black.getName() + ", imate " + (black.getNumberOfWins() - white.getNumberOfWins()) + " pobeda vise i " + (((100 * black.getNumberOfWins()) / algorithm.getTimes()) - ((100 * white.getNumberOfWins()) / algorithm.getTimes())) + "% sanse vise");
        } else if (black.getNumberOfWins() < white.getNumberOfWins()) {
            Component.getInstance().getResultL().setText("Cestitamo, " + white.getName() + ", imate " + (white.getNumberOfWins() - black.getNumberOfWins()) + " pobeda vise i " + (((100 * white.getNumberOfWins()) / algorithm.getTimes()) - ((100 * black.getNumberOfWins()) / algorithm.getTimes())) + "% sanse vise");
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

        //
        Component.getInstance().getResultL().setText("Playing...");

        while(true) {
            // Check if table is full
            if (checkFullGame(algorithm)) {
                break;
            }

            // Add and check if someone winn
            int add = add(algorithm, black, white);
            if (add == Storage.ADD_FULL) {
                break;
            } else if (add == Storage.ADD_FOUNDED) {
                break;
            }
        }

        return new int[0];
    }

    @Override
    public void show(Algorithm algorithm) {
        for(int i = 0; i < algorithm.getRow(); i++) {
            for(int j = 0; j < algorithm.getCol(); j++) {
                System.out.print(algorithm.getGame()[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public int add(Algorithm algorithm, Token black, Token white) {
        while (true) {
            if (checkFullGame(algorithm)) {
                System.out.println("[Operations][add()] Popunjeno");

                // Check if there is no winner
                if (!black.isWinner() && !white.isWinner()) {
                    System.out.println("[Operations][add()] Nema pobednika");
                }
                return Storage.ADD_FULL;
            }
            int random = ThreadLocalRandom.current().nextInt(0, algorithm.getCol());
            int i = 0;

            // Check if repeated random
            if (!algorithm.getFullColumn()[random]) {
                continue;
            }
            while (true) {
                // Check if column is full
                if (algorithm.getGame()[i][random] == Storage.BLACK || algorithm.getGame()[i][random] == Storage.WHITE) {
                    algorithm.getFullColumn()[random] = false;
                    algorithm.setFullColumn(algorithm.getFullColumn());
                    break;
                }

                // Check if next row in column is bigger than last
                if (i + 1 >= algorithm.getRow()) {
                    // if connect5 was founded
                    if (!addInGame(algorithm, black, white, i, random)) {
                        // Add winner
                        addWinner(algorithm, black, white, i, random);
                        return Storage.ADD_FOUNDED;
                    }
                    break;
                }

                // Check if next row in column is busy
                if (algorithm.getGame()[i + 1][random] == Storage.BLACK || algorithm.getGame()[i + 1][random] == Storage.WHITE) {

                    // if connect5 was founded
                    if (!addInGame(algorithm, black, white, i, random)) {
                        // Add winner
                        addWinner(algorithm, black, white, i, random);
                        return Storage.ADD_FOUNDED;
                    }
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
        return Storage.ADD_NEXT;
    }

    @Override
    public void addWinner(Algorithm algorithm, Token black, Token white, int row, int col) {
        algorithm.setRowWinner(row);
        algorithm.setColWinner(col);


        // if black win
        if (algorithm.getGame()[algorithm.getRowWinner()][algorithm.getColWinner()] == Storage.BLACK) {
            black.setNumberOfWins(black.getNumberOfWins() + 1);
        }

        // if white win
        if (algorithm.getGame()[algorithm.getRowWinner()][algorithm.getColWinner()] == Storage.WHITE) {
            white.setNumberOfWins(white.getNumberOfWins() + 1);
        }
    }

    @Override
    public boolean addInGame(Algorithm algorithm, Token black, Token white, int row, int col) {
        if (black.getTurn()) {
            algorithm.getGame()[row][col] = Storage.BLACK;
            black.setTurn(false);
            white.setTurn(true);
        } else {
            algorithm.getGame()[row][col] = Storage.WHITE;
            black.setTurn(true);
            white.setTurn(false);
        }

        algorithm.setGame(algorithm.getGame());

        // if connect5 was founded
        if (check(algorithm, row, col)) {

            // Check who is winner
            if (algorithm.getGame()[row][col] == Storage.BLACK) {
                black.setWinner(true);
            } else if (algorithm.getGame()[row][col] == Storage.WHITE) {
                white.setWinner(true);
            }
            return false;
        }

        return true;
    }

    @Override
    public boolean check(Algorithm algorithm, int row, int col) {
        // Number of possible cases
        int cases = 4;

        // Counter
        int i = 1;

        // Connects
        int connects = 1;

        boolean left = true;
        boolean right = true;


        while (true) {
            // Check if last case is done
            if (i > cases) {
                break;
            }

            if (i == 1) {
                // First case --

                int currentRow = row;
                int currentCol = col;

                while (true) {
                    if (left) {
                        // Shift to the left
                        currentCol = currentCol - 1;

                        // Check if still in a game
                        if (currentCol >= 0) {
                            if (algorithm.getGame()[row][currentCol] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Horizontalno");
                                    return true;
                                }
                            } else {
                                // Reset currentCol, try right side
                                currentCol = col;

                                left = false;
                                right = true;
                            }
                        } else {
                            // Reset currentCol, try right side
                            currentCol = col;

                            left = false;
                            right = true;
                        }
                    } else if (right) {
                        // Shift to the right
                        currentCol = currentCol + 1;

                        // Check if still in a game
                        if (currentCol < algorithm.getCol()) {
                            if (algorithm.getGame()[row][currentCol] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Horizontalno");
                                    return true;
                                }
                            } else {
                                i++;
                                left = true;
                                right = true;
                                break;
                            }
                        } else {
                            i++;
                            left = true;
                            right = true;
                            break;
                        }
                    }
                }
                connects = 1;
            } else if (i == 2) {
                // Second case |

                int currentRow = row;
                int currentCol = col;

                while (true) {
                    if (left) {
                        // Shift to the left
                        currentRow = currentRow - 1;

                        // Check if still in a game
                        if (currentRow >= 0) {
                            if (algorithm.getGame()[currentRow][col] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Vertikalno");
                                    return true;
                                }
                            } else {
                                // Reset currentCol, try right side
                                currentRow = row;

                                left = false;
                                right = true;
                            }
                        } else {
                            // Reset currentCol, try right side
                            currentRow = row;

                            left = false;
                            right = true;
                        }
                    } else if (right) {
                        // Shift to the right
                        currentRow = currentRow + 1;

                        // Check if still in a game
                        if (currentRow < algorithm.getRow()) {
                            if (algorithm.getGame()[currentRow][col] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Vertikalno");
                                    return true;
                                }
                            } else {
                                i++;
                                left = true;
                                right = true;
                                break;
                            }
                        } else {
                            i++;
                            left = true;
                            right = true;
                            break;
                        }
                    }
                }
                connects = 1;
            } else if (i == 3) {
                // Third case \

                int currentRow = row;
                int currentCol = col;

                while (true) {
                    if (left) {
                        // Shift to the left
                        currentRow = currentRow - 1;
                        currentCol = currentCol - 1;

                        // Check if still in a game
                        if (currentRow >= 0 && currentCol >= 0) {
                            if (algorithm.getGame()[currentRow][currentCol] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Ukoso levo gore, desno dole");
                                    return true;
                                }
                            } else {
                                // Reset currentCol, try right side
                                currentRow = row;
                                currentCol = col;

                                left = false;
                                right = true;
                            }
                        } else {
                            // Reset currentCol, try right side
                            currentRow = row;
                            currentCol = col;

                            left = false;
                            right = true;
                        }
                    } else if (right) {
                        // Shift to the right
                        currentRow = currentRow + 1;
                        currentCol = currentCol + 1;

                        // Check if still in a game
                        if (currentRow < algorithm.getRow() && currentCol < algorithm.getCol()) {
                            if (algorithm.getGame()[currentRow][currentCol] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Ukoso levo gore, desno dole");
                                    return true;
                                }
                            } else {
                                i++;
                                left = true;
                                right = true;
                                break;
                            }
                        } else {
                            i++;
                            left = true;
                            right = true;
                            break;
                        }
                    }
                }
                connects = 1;
            } else if (i == 4) {
                // Forth case /

                int currentRow = row;
                int currentCol = col;

                while (true) {
                    if (left) {
                        // Shift to the left
                        currentRow = currentRow + 1;
                        currentCol = currentCol + 1;

                        // Check if still in a game
                        if (currentRow < algorithm.getRow() && currentCol < algorithm.getCol()) {
                            if (algorithm.getGame()[currentRow][currentCol] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Ukoso desno gore, levo dole");
                                    return true;
                                }
                            } else {
                                // Reset currentCol, try right side
                                currentRow = row;
                                currentCol = col;

                                left = false;
                                right = true;
                            }
                        } else {
                            // Reset currentCol, try right side
                            currentRow = row;
                            currentCol = col;

                            left = false;
                            right = true;
                        }
                    } else if (right) {
                        // Shift to the right
                        currentRow = currentRow - 1;
                        currentCol = currentCol - 1;

                        // Check if still in a game
                        if (currentRow >= 0 && currentCol >= 0) {
                            if (algorithm.getGame()[currentRow][currentCol] == algorithm.getGame()[row][col]) {
                                // One more connect
                                connects++;

                                if (connects == 5) {
                                    System.out.println("[Operations][check()] Ukoso desno gore, levo dole");
                                    return true;
                                }
                            } else {
                                i++;
                                left = true;
                                right = true;
                                break;
                            }
                        } else {
                            i++;
                            left = true;
                            right = true;
                            break;
                        }
                    }
                }
                connects = 1;
            }
        }

        return false;
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
                if (algorithm.getGame()[i][j] == Storage.BLACK) {
                    black.setFields(black.getFields() + 1);
                } else if (algorithm.getGame()[i][j] == Storage.WHITE) {
                    white.setFields(white.getFields() + 1);
                }
            }
        }

        if (black.getFields() < white.getFields()) {
            System.out.println("[Operations][turn()] Black token has first turn.");
            black.setTurn(true);
        } else {
            System.out.println("[Operations][turn()] White token has first turn.");
            white.setTurn(true);
        }
    }

    @Override
    public void reset(Algorithm algorithm, Token black, Token white) {

        algorithm.setRowWinner(0);
        algorithm.setColWinner(0);
        algorithm.setFullColumnTrue();
        algorithm.setGame(new int[algorithm.getRow()][algorithm.getCol()]);

        black.setWinner(false);
        white.setWinner(false);
    }
}
