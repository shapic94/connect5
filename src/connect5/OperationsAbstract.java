package connect5;

import entity.Token;

/**
 * Created by nebojsa.sapic on 8/9/17.
 */

public interface OperationsAbstract {

    int[] test(Algorithm algorithm, Token black, Token white);
    boolean testSameColumn(int[] columnHasToken);
    int[] init(String player1, String player2, int row, int col, int tokens, int times);
    int[] play(Algorithm algorithm, Token black, Token white);
    void show(Algorithm algorithm);
    int add(Algorithm algorithm, Token black, Token white);
    void addWinner(Algorithm algorithm, Token black, Token white, int row, int col);
    boolean addInGame(Algorithm algorithm, Token black, Token white, int row, int col);
    boolean check(Algorithm algorithm, int row, int col);
    boolean checkFullGame(Algorithm algorithm);
    void turn(Algorithm algorithm, Token black, Token white);
    void reset(Algorithm algorithm, Token black, Token white);
}