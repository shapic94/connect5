package connect5;

/**
 * Created by nebojsa.sapic on 8/8/17.
 */
public class Algorithm extends Operations {

    private int row;
    private int col;
    private int[][] game;
    private int[] result;

    public Algorithm(int row, int col, int[][] game) {
        this.row = row;
        this.col = col;
        this.game = game;

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
}
