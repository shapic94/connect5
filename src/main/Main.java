package main;

import connect5.Operations;
import global.Storage;
import gui.Component;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import servent.ServentListener;
import servent.SocketUtils;

import java.io.IOException;
import java.net.Socket;

import java.awt.*;


/**
 * Created by nebojsa.sapic on 8/8/17.
 *
 * Black = 1
 * White = 2
 */

public class Main extends Application {

    private static final Border black = new Border(new BorderStroke(Color.BLACK,
            BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2)));

    public static void main(String [] args) {
          System.out.println("Welcome to connect5 game");
          launch(args);
    }
  
    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane bp = new BorderPane();
        Scene scene = new Scene(bp,950,1200);
        GridPane gp = new GridPane();
        FlowPane layout = new FlowPane();
        bp.setTop(layout);
        bp.setBottom(gp);

        layout.setHgap(25);
        layout.setPadding(new Insets(16));
        layout.setBorder(black);
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        gp.setBorder(black);
        gp.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        gp.setAlignment(Pos.BOTTOM_CENTER);

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Marko", 35),
                        new PieChart.Data("Aleksandar", 45),
                        new PieChart.Data("Nebojsa", 20));

        final PieChart chart = new PieChart(pieChartData);
        //chart.setLabelLineLength(10);
        //chart.setLegendSide(Side.LEFT);
        //chart.setTitle("Connect5");

        final Label caption = new Label("");
        caption.setTextFill(Color.RED);
        caption.setStyle("-fx-font: 24 arial;");
//        caption.set

        for(final PieChart.Data data: chart.getData()){
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println(data.getPieValue());
                            caption.setTranslateX(50);
                            caption.setTranslateY(50);
//                            caption.
                            caption.setText(String.valueOf(data.getPieValue()) + "%");
                            System.out.println(caption.getText());
                        }
                    });
        }
        ((BorderPane) scene.getRoot()).getChildren().addAll(chart, caption);

//        scene.getRoot().getChildrenUnmodifiable().addAll(chart, caption);

//        ((Group) scene.getRoot()).getChildren().add(chart);
        gp.add(chart,1,1);

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);


        // first player
        Component.getInstance().getPlayer1HB().getChildren().addAll(Component.getInstance().getPlayer1L(), Component.getInstance().getPlayer1TF());
        Component.getInstance().getPlayer1HB().setAlignment(Pos.CENTER);
        Component.getInstance().getPlayer1HB().setSpacing(20);
        Component.getInstance().getPlayer1HB().setPrefHeight(50);
        Component.getInstance().getPlayer1HB().setPrefWidth(640);
        Component.getInstance().getPlayer1L().setPrefWidth(240);
        Component.getInstance().getPlayer1TF().setPrefWidth(400);

        // second player
        Component.getInstance().getPlayer2HB().getChildren().addAll(Component.getInstance().getPlayer2L(), Component.getInstance().getPlayer2TF());
        Component.getInstance().getPlayer2HB().setAlignment(Pos.CENTER);
        Component.getInstance().getPlayer2HB().setSpacing(20);
        Component.getInstance().getPlayer2HB().setPrefHeight(50);
        Component.getInstance().getPlayer2HB().setPrefWidth(640);
        Component.getInstance().getPlayer2L().setPrefWidth(240);
        Component.getInstance().getPlayer2TF().setPrefWidth(400);

        // number of rows
        Component.getInstance().getRowHB().getChildren().addAll(Component.getInstance().getRowL(), Component.getInstance().getRowTF());
        Component.getInstance().getRowHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getRowHB().setSpacing(20);
        Component.getInstance().getRowHB().setPrefHeight(50);
        Component.getInstance().getRowHB().setPrefWidth(640);
        Component.getInstance().getRowL().setPrefWidth(240);
        Component.getInstance().getRowTF().setPrefWidth(400);

        // number of columns
        Component.getInstance().getColHB().getChildren().addAll(Component.getInstance().getColL(), Component.getInstance().getColTF());
        Component.getInstance().getColHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getColHB().setSpacing(20);
        Component.getInstance().getColHB().setPrefHeight(50);
        Component.getInstance().getColHB().setPrefWidth(640);
        Component.getInstance().getColL().setPrefWidth(240);
        Component.getInstance().getColTF().setPrefWidth(400);

        // number of tokens
        Component.getInstance().getTokenHB().getChildren().addAll(Component.getInstance().getTokensL(), Component.getInstance().getTokensTF());
        Component.getInstance().getTokenHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getTokenHB().setSpacing(20);
        Component.getInstance().getTokenHB().setPrefHeight(50);
        Component.getInstance().getTokenHB().setPrefWidth(640);
        Component.getInstance().getTokensL().setPrefWidth(240);
        Component.getInstance().getTokensTF().setPrefWidth(400);

        // number of times
        Component.getInstance().getTimesHB().getChildren().addAll(Component.getInstance().getTimesL(), Component.getInstance().getTimesTF());
        Component.getInstance().getTimesHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getTimesHB().setSpacing(20);
        Component.getInstance().getTimesHB().setPrefHeight(50);
        Component.getInstance().getTimesHB().setPrefWidth(640);
        Component.getInstance().getTimesL().setPrefWidth(240);
        Component.getInstance().getTimesTF().setPrefWidth(400);

        // button
        Component.getInstance().getStartHB().getChildren().addAll(Component.getInstance().getStartL(), Component.getInstance().getStartB());
        Component.getInstance().getStartHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getStartHB().setSpacing(20);
        Component.getInstance().getStartHB().setPrefHeight(50);
        Component.getInstance().getStartHB().setPrefWidth(640);
        Component.getInstance().getStartL().setPrefWidth(240);
        Component.getInstance().getStartB().setPrefWidth(400);

        Component.getInstance().getPlayer1BoxHB().getChildren().addAll(Component.getInstance().getPlayer1NameL(), Component.getInstance().getPlayer1ScoreL());
        Component.getInstance().getPlayer1BoxHB().setAlignment(Pos.CENTER);
        Component.getInstance().getPlayer1BoxHB().setSpacing(20);
        Component.getInstance().getPlayer1BoxHB().setPrefHeight(50);
        Component.getInstance().getPlayer1BoxHB().setPrefWidth(640);
        Component.getInstance().getPlayer1NameL().setPrefWidth(240);
        Component.getInstance().getPlayer1ScoreL().setPrefWidth(400);

        Component.getInstance().getPlayer2BoxHB().getChildren().addAll(Component.getInstance().getPlayer2NameL(), Component.getInstance().getPlayer2ScoreL());
        Component.getInstance().getPlayer2BoxHB().setAlignment(Pos.CENTER);
        Component.getInstance().getPlayer2BoxHB().setSpacing(20);
        Component.getInstance().getPlayer2BoxHB().setPrefHeight(50);
        Component.getInstance().getPlayer2BoxHB().setPrefWidth(640);
        Component.getInstance().getPlayer2NameL().setPrefWidth(240);
        Component.getInstance().getPlayer2ScoreL().setPrefWidth(400);

        Component.getInstance().getResultHB().getChildren().add(Component.getInstance().getResultL());
        Component.getInstance().getTokenHB().setAlignment(Pos.CENTER);
        Component.getInstance().getTokenHB().setSpacing(20);
        Component.getInstance().getTokenHB().setPrefHeight(50);
        Component.getInstance().getTokenHB().setPrefWidth(640);
        Component.getInstance().getResultL().setPrefWidth(640);


        ObservableList list = layout.getChildren();
        list.addAll(
                Component.getInstance().getPlayer1HB(),
                Component.getInstance().getPlayer2HB(),
                Component.getInstance().getRowHB(),
                Component.getInstance().getColHB(),
                Component.getInstance().getTokenHB(),
                Component.getInstance().getTimesHB(),
                Component.getInstance().getStartHB(),
                Component.getInstance().getPlayer1BoxHB(),
                Component.getInstance().getPlayer2BoxHB(),
                Component.getInstance().getResultHB()
        );

//
//        Component.getInstance().getStartB().setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                Socket socket = null;
//                try {
//                    String player1 = Component.getInstance().getPlayer1TF().getText();
//                    String player2 = Component.getInstance().getPlayer2TF().getText();
//                    int row = Integer.parseInt(Component.getInstance().getRowTF().getText());
//                    int col = Integer.parseInt(Component.getInstance().getColTF().getText());
//                    int tokens = Integer.parseInt(Component.getInstance().getTokensTF().getText());
//                    int times = Integer.parseInt(Component.getInstance().getTimesTF().getText());
//
////                    if (!player1.equals("") && !player2.equals("") && row != 0 && col != 0 && tokens != 0 && times != 0) {
////                        socket = new Socket(Storage.BOOTSTRAP_IP, Storage.BOOTSTRAP_PORT);
////
////                        // GUI_INFO ip:port player1,player2,row,col,tokens,times
////                        SocketUtils.writeLine(
////                                socket,
////                                Storage.GUI_INFO + " " +
////                                socket.getInetAddress().getHostAddress() + ":" +
////                                socket.getPort() + " " +
////                                player1 + "," +
////                                player2 + "," +
////                                row + "," +
////                                col + "," +
////                                tokens + "," +
////                                times + ",");
////                    }
//
////                    // Initializing
////                    Object[] object = new Operations().init(player1, player2, row, col, tokens, times);
//
//                } catch (NumberFormatException err) {
//                    System.out.println("Greska");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}