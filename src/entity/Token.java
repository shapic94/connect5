package entity;

public class Token {
    private boolean turn;
    private int fields;

    public static final int COUNT = 2;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    public Token(int fields, boolean turn) {
        this.turn = turn;
        this.fields = fields;
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
}
