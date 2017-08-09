package connect5;

/**
 * Created by nebojsa.sapic on 8/8/17.
 */
public class Algorithm {

    private int col;
    private int row;
    private int result;

    public Algorithm(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
