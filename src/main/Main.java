package main;

import connect5.Operations;
import global.Storage;
import gui.Component;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import servent.ServentListener;
import servent.SocketUtils;

import java.io.IOException;
import java.net.Socket;


/**
 * Created by nebojsa.sapic on 8/8/17.
 *
 * Black = 1
 * White = 2
 */

public class Main extends Application {

    public static void main(String [] args) {
        System.out.println("Welcome to connect5 game");
        launch(args);

//        // Rows
//        int row = 10;
//
//        // Columns
//        int col = 10;
//
//        // Tokens
//        int tokens = 5;
//
//        // Times
//        int times = 1;

//        JFrame frame = new JFrame("Connect5");
//        frame.setSize(640, 480);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        Frame.getInstance().add(Panel.getInstance());

//        Panel.getGroupLayout().setHorizontalGroup(
//                Panel.getGroupLayout().createSequentialGroup()
//                .addComponent(Component.getInstance().getRowL())
//                .addComponent(Component.getInstance().getRowTF())
//        );
//        Panel.getGroupLayout().createSequentialGroup().addGroup(
//                Panel.getGroupLayout().createParallelGroup()
//                .addComponent(Component.getInstance().getRowL())
//                .addComponent(Component.getInstance().getRowTF())
//        );
//        Panel.getInstance().add(Component.getInstance().getRowL());
//        Panel.getInstance().add(Component.getInstance().getRowTF());
//
//        Panel.getInstance().add(Component.getInstance().getColL());
//        Panel.getInstance().add(Component.getInstance().getColTF());
//
//        Panel.getInstance().add(Component.getInstance().getTokensL());
//        Panel.getInstance().add(Component.getInstance().getTokensTF());
//
//        Panel.getInstance().add(Component.getInstance().getTimesL());
//        Panel.getInstance().add(Component.getInstance().getTimesTF());
//
//        Panel.getInstance().add(Component.getInstance().getStartB());
//
//        Panel.getInstance().add(Component.getInstance().getResultL());

//        Component.getInstance().getStartB().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    int row = Integer.parseInt(Component.getInstance().getRowTF().getText());
//                    int col = Integer.parseInt(Component.getInstance().getColTF().getText());
//                    int tokens = Integer.parseInt(Component.getInstance().getTokensTF().getText());
//                    int times = Integer.parseInt(Component.getInstance().getTimesTF().getText());
//
//
//                    // Initializing
//                    Object[] object = new Operations().init(row, col, tokens, times);
//
//                } catch (NumberFormatException err) {
//                    System.out.println("Greska");
//                }
//            }
//        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FlowPane layout = new FlowPane();

        layout.setHgap(25);

        Component.getInstance().getPlayer1HB().getChildren().addAll(Component.getInstance().getPlayer1L(), Component.getInstance().getPlayer1TF());
        Component.getInstance().getPlayer1HB().setAlignment(Pos.CENTER);
        Component.getInstance().getPlayer1HB().setSpacing(20);
        Component.getInstance().getPlayer1HB().setPrefHeight(50);
        Component.getInstance().getPlayer1HB().setPrefWidth(640);
        Component.getInstance().getPlayer1L().setPrefWidth(240);
        Component.getInstance().getPlayer1TF().setPrefWidth(400);

        Component.getInstance().getPlayer2HB().getChildren().addAll(Component.getInstance().getPlayer2L(), Component.getInstance().getPlayer2TF());
        Component.getInstance().getPlayer2HB().setAlignment(Pos.CENTER);
        Component.getInstance().getPlayer2HB().setSpacing(20);
        Component.getInstance().getPlayer2HB().setPrefHeight(50);
        Component.getInstance().getPlayer2HB().setPrefWidth(640);
        Component.getInstance().getPlayer2L().setPrefWidth(240);
        Component.getInstance().getPlayer2TF().setPrefWidth(400);

        Component.getInstance().getRowHB().getChildren().addAll(Component.getInstance().getRowL(), Component.getInstance().getRowTF());
        Component.getInstance().getRowHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getRowHB().setSpacing(20);
        Component.getInstance().getRowHB().setPrefHeight(50);
        Component.getInstance().getRowHB().setPrefWidth(640);
        Component.getInstance().getRowL().setPrefWidth(240);
        Component.getInstance().getRowTF().setPrefWidth(400);

        Component.getInstance().getColHB().getChildren().addAll(Component.getInstance().getColL(), Component.getInstance().getColTF());
        Component.getInstance().getColHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getColHB().setSpacing(20);
        Component.getInstance().getColHB().setPrefHeight(50);
        Component.getInstance().getColHB().setPrefWidth(640);
        Component.getInstance().getColL().setPrefWidth(240);
        Component.getInstance().getColTF().setPrefWidth(400);

        Component.getInstance().getTokenHB().getChildren().addAll(Component.getInstance().getTokensL(), Component.getInstance().getTokensTF());
        Component.getInstance().getTokenHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getTokenHB().setSpacing(20);
        Component.getInstance().getTokenHB().setPrefHeight(50);
        Component.getInstance().getTokenHB().setPrefWidth(640);
        Component.getInstance().getTokensL().setPrefWidth(240);
        Component.getInstance().getTokensTF().setPrefWidth(400);

        Component.getInstance().getTimesHB().getChildren().addAll(Component.getInstance().getTimesL(), Component.getInstance().getTimesTF());
        Component.getInstance().getTimesHB().setAlignment(Pos.CENTER_LEFT);
        Component.getInstance().getTimesHB().setSpacing(20);
        Component.getInstance().getTimesHB().setPrefHeight(50);
        Component.getInstance().getTimesHB().setPrefWidth(640);
        Component.getInstance().getTimesL().setPrefWidth(240);
        Component.getInstance().getTimesTF().setPrefWidth(400);

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


        Scene scene = new Scene(layout, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);


        Component.getInstance().getStartB().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Socket socket = null;
                try {
                    String player1 = Component.getInstance().getPlayer1TF().getText();
                    String player2 = Component.getInstance().getPlayer2TF().getText();
                    int row = Integer.parseInt(Component.getInstance().getRowTF().getText());
                    int col = Integer.parseInt(Component.getInstance().getColTF().getText());
                    int tokens = Integer.parseInt(Component.getInstance().getTokensTF().getText());
                    int times = Integer.parseInt(Component.getInstance().getTimesTF().getText());

                    if (!player1.equals("") && !player2.equals("") && row != 0 && col != 0 && tokens != 0 && times != 0) {
                        socket = new Socket(Storage.BOOTSTRAP_IP, Storage.BOOTSTRAP_PORT);

                        // GUI_INFO ip:port player1,player2,row,col,tokens,times
                        SocketUtils.writeLine(
                                socket,
                                Storage.GUI_INFO + " " +
                                socket.getInetAddress().getHostAddress() + ":" +
                                socket.getPort() + " " +
                                player1 + "," +
                                player2 + "," +
                                row + "," +
                                col + "," +
                                tokens + "," +
                                times + ",");
                    }

//                    // Initializing
//                    Object[] object = new Operations().init(player1, player2, row, col, tokens, times);

                } catch (NumberFormatException err) {
                    System.out.println("Greska");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}