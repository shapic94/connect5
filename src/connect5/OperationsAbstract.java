package connect5;

import entity.Token;

/**
 * Created by nebojsa.sapic on 8/9/17.
 */

public interface OperationsAbstract {

    int[] play(Algorithm algorithm);
    void show(Algorithm algorithm);
    void add(Algorithm algorithm, Token black, Token white);
    void addInGame(Algorithm algorithm, Token black, Token white, int random, int i);
    void check(Algorithm algorithm);
    void turn(Algorithm algorithm, Token black, Token white);
}