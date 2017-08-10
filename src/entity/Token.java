package entity;

public class Token {
    private boolean turn;
    private int fields;
    private boolean winner;
    private String name;

    public static final int COUNT = 2;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    public Token(String name, int fields, boolean turn, boolean winner) {
        this.name = name;
        this.turn = turn;
        this.fields = fields;
        this.winner = winner;
    }

    public int getFields() {
        return fields;
    }

    public void setFields(int fields) {
        this.fields = fields;
    }


    public boolean getTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
