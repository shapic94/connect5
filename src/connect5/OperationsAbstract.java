package connect5;

import entity.Token;

/**
 * Created by nebojsa.sapic on 8/9/17.
 */

public interface OperationsAbstract {

    void test(Algorithm algorithm, Token black, Token white);
    boolean testSameColumn(int[] columnHasToken);
    Object[] init(int row, int col, int tokens, int times);
    int[] play(Algorithm algorithm, Token black, Token white);
    void show(Algorithm algorithm);
    void add(Algorithm algorithm, Token black, Token white);
    void addInGame(Algorithm algorithm, Token black, Token white, int random, int i);
    void check(Algorithm algorithm);
    boolean checkFullGame(Algorithm algorithm);
    void turn(Algorithm algorithm, Token black, Token white);
}