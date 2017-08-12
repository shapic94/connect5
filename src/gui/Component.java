package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Component {

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
    private Button startB = new Button("Start");

    private HBox resultHB = new HBox();
    private Label resultL = new Label("Waiting...");

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


}
