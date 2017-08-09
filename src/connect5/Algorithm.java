package connect5;

import java.util.Arrays;

/**
 * Created by nebojsa.sapic on 8/8/17.
 */
public class Algorithm extends Operations {

    private int row;
    private int col;
    private int[][] game;
    private int[] result;
    private boolean[] fullColumn;

    public Algorithm(int row, int col, int[][] game, boolean[] fullColumn) {
        this.row = row;
        this.col = col;
        this.game = game;
        this.fullColumn = fullColumn;
        Arrays.fill(this.fullColumn, true);

        this.result = play(this);
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
}
