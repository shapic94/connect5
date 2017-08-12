package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Component {

    private HBox player1HB = new HBox();
    private Label player1L = new Label("First player : ");
    private TextField player1TF = new TextField();

    private HBox player2HB = new HBox();
    private Label player2L = new Label("Second player : ");
    private TextField player2TF = new TextField();

    private HBox rowHB = new HBox();
    private Label rowL = new Label("Number of rows : ");
    private TextField rowTF = new TextField();

    private HBox colHB = new HBox();
    private Label colL = new Label("Number of columns : ");
    private TextField colTF = new TextField();

    private HBox tokenHB = new HBox();
    private Label tokensL = new Label("Number of tokens : ");
    private TextField tokensTF = new TextField();

    private HBox timesHB = new HBox();
    private Label timesL = new Label("Number of times : ");
    private TextField timesTF = new TextField();

    private HBox startHB = new HBox();
    private Label startL = new Label();
    private Button startB = new Button("Start");

    private HBox resultHB = new HBox();
    private Label resultL = new Label();

    private HBox player1BoxHB = new HBox();
    private Label player1NameL = new Label();
    private Label player1ScoreL = new Label();

    private HBox player2BoxHB = new HBox();
    private Label player2NameL = new Label();
    private Label player2ScoreL = new Label();

    private static Component myInstance;

    public Component() {

    }

    public static Component getInstance() {
        if (myInstance == null) {
            myInstance = new Component();
        }

        return myInstance;
    }

    public Label getRowL() {
        return rowL;
    }

    public void setRowL(Label rowL) {
        this.rowL = rowL;
    }

    public TextField getRowTF() {
        return rowTF;
    }

    public void setRowTF(TextField rowTF) {
        this.rowTF = rowTF;
    }

    public Label getColL() {
        return colL;
    }

    public void setColL(Label colL) {
        this.colL = colL;
    }

    public TextField getColTF() {
        return colTF;
    }

    public void setColTF(TextField colTF) {
        this.colTF = colTF;
    }

    public Label getTokensL() {
        return tokensL;
    }

    public void setTokensL(Label tokensL) {
        this.tokensL = tokensL;
    }

    public TextField getTokensTF() {
        return tokensTF;
    }

    public void setTokensTF(TextField tokensTF) {
        this.tokensTF = tokensTF;
    }

    public Label getTimesL() {
        return timesL;
    }

    public void setTimesL(Label timesL) {
        this.timesL = timesL;
    }

    public TextField getTimesTF() {
        return timesTF;
    }

    public void setTimesTF(TextField timesTF) {
        this.timesTF = timesTF;
    }

    public Button getStartB() {
        return startB;
    }

    public void setStartB(Button startB) {
        this.startB = startB;
    }

    public Label getResultL() {
        return resultL;
    }

    public void setResultL(Label resultL) {
        this.resultL = resultL;
    }

    public HBox getRowHB() {
        return rowHB;
    }

    public void setRowHB(HBox rowHB) {
        this.rowHB = rowHB;
    }

    public HBox getColHB() {
        return colHB;
    }

    public void setColHB(HBox colHB) {
        this.colHB = colHB;
    }

    public HBox getTokenHB() {
        return tokenHB;
    }

    public void setTokenHB(HBox tokenHB) {
        this.tokenHB = tokenHB;
    }

    public HBox getTimesHB() {
        return timesHB;
    }

    public void setTimesHB(HBox timesHB) {
        this.timesHB = timesHB;
    }

    public HBox getResultHB() {
        return resultHB;
    }

    public void setResultHB(HBox resultHB) {
        this.resultHB = resultHB;
    }

    public HBox getStartHB() {
        return startHB;
    }

    public void setStartHB(HBox startHB) {
        this.startHB = startHB;
    }

    public HBox getPlayer1HB() {
        return player1HB;
    }

    public void setPlayer1HB(HBox player1HB) {
        this.player1HB = player1HB;
    }

    public Label getPlayer1NameL() {
        return player1NameL;
    }

    public void setPlayer1NameL(Label player1NameL) {
        this.player1NameL = player1NameL;
    }

    public Label getPlayer1ScoreL() {
        return player1ScoreL;
    }

    public void setPlayer1ScoreL(Label player1ScoreL) {
        this.player1ScoreL = player1ScoreL;
    }

    public HBox getPlayer2HB() {
        return player2HB;
    }

    public void setPlayer2HB(HBox player2HB) {
        this.player2HB = player2HB;
    }

    public Label getPlayer2NameL() {
        return player2NameL;
    }

    public void setPlayer2NameL(Label player2NameL) {
        this.player2NameL = player2NameL;
    }

    public Label getPlayer2ScoreL() {
        return player2ScoreL;
    }

    public void setPlayer2ScoreL(Label player2ScoreL) {
        this.player2ScoreL = player2ScoreL;
    }

    public Label getPlayer1L() {
        return player1L;
    }

    public void setPlayer1L(Label player1L) {
        this.player1L = player1L;
    }

    public TextField getPlayer1TF() {
        return player1TF;
    }

    public void setPlayer1TF(TextField player1TF) {
        this.player1TF = player1TF;
    }

    public Label getPlayer2L() {
        return player2L;
    }

    public void setPlayer2L(Label player2L) {
        this.player2L = player2L;
    }

    public TextField getPlayer2TF() {
        return player2TF;
    }

    public void setPlayer2TF(TextField player2TF) {
        this.player2TF = player2TF;
    }

    public HBox getPlayer1BoxHB() {
        return player1BoxHB;
    }

    public void setPlayer1BoxHB(HBox player1BoxHB) {
        this.player1BoxHB = player1BoxHB;
    }

    public HBox getPlayer2BoxHB() {
        return player2BoxHB;
    }

    public void setPlayer2BoxHB(HBox player2BoxHB) {
        this.player2BoxHB = player2BoxHB;
    }

    public Label getStartL() {
        return startL;
    }

    public void setStartL(Label startL) {
        this.startL = startL;
    }
}
