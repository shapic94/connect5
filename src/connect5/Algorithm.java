package connect5;

import java.util.Arrays;

/**
 * Created by nebojsa.sapic on 8/8/17.
 */
public class Algorithm extends Operations {

    private int row;
    private int col;
    private int tokens;
    private int times;
    private int[][] game;
    private int[] result;
    private boolean[] fullColumn;

    public Algorithm() {

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int[][] getGame() {
        return game;
    }

    public void setGame(int[][] game) {
        this.game = game;
    }

    public int[] getResult() {
        return result;
    }

    public void setResult(int[] result) {
        this.result = result;
    }

    public boolean[] getFullColumn() {
        return fullColumn;
    }

    public void setFullColumn(boolean[] fullColumn) {
        this.fullColumn = fullColumn;
    }

    public void setFullColumnTrue() { Arrays.fill(this.fullColumn, true); }

    public int getTokens() { return tokens; }

    public void setTokens(int tokens) { this.tokens = tokens; }

    public int getTimes() { return times; }

    public void setTimes(int times) { this.times = times; }
}
