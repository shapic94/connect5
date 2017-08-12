package entity;

public class Token {
    private boolean turn;
    private int fields;
    private boolean winner;
    private int numberOfWins;
    private String name;

    public Token(String name, int fields, boolean turn, boolean winner, int numberOfWins) {
        this.name = name;
        this.turn = turn;
        this.fields = fields;
        this.winner = winner;
        this.numberOfWins = numberOfWins;
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

    public boolean isTurn() {
        return turn;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }
}
