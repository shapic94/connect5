package main;

//import gui.FX;
import connect5.Operations;
import gui.Component;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * Created by nebojsa.sapic on 8/8/17.
 *
 * Black = 1
 * White = 2
 */
public class Main extends Application {

    public static void main(String [] args) {
//        launch(args);
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

        Component.getInstance().getStartHB().getChildren().add(Component.getInstance().getStartB());
        Component.getInstance().getTokenHB().setAlignment(Pos.CENTER);
        Component.getInstance().getTokenHB().setSpacing(20);
        Component.getInstance().getTokenHB().setPrefHeight(50);
        Component.getInstance().getTokenHB().setPrefWidth(640);
        Component.getInstance().getStartB().setPrefWidth(250);

        Component.getInstance().getResultHB().getChildren().add(Component.getInstance().getResultL());
        Component.getInstance().getTokenHB().setAlignment(Pos.CENTER);
        Component.getInstance().getTokenHB().setSpacing(20);
        Component.getInstance().getTokenHB().setPrefHeight(50);
        Component.getInstance().getTokenHB().setPrefWidth(640);
        Component.getInstance().getResultL().setPrefWidth(640);


        ObservableList list = layout.getChildren();
        list.addAll(
                Component.getInstance().getRowHB(),
                Component.getInstance().getColHB(),
                Component.getInstance().getTokenHB(),
                Component.getInstance().getTimesHB(),
                Component.getInstance().getStartHB(),
                Component.getInstance().getResultHB()
        );


        Scene scene = new Scene(layout, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);


        Component.getInstance().getStartB().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int row = Integer.parseInt(Component.getInstance().getRowTF().getText());
                    int col = Integer.parseInt(Component.getInstance().getColTF().getText());
                    int tokens = Integer.parseInt(Component.getInstance().getTokensTF().getText());
                    int times = Integer.parseInt(Component.getInstance().getTimesTF().getText());

                    System.out.println(row + " " + " "+col+" "+tokens+" "+times);

                    // Initializing
                    Object[] object = new Operations().init(row, col, tokens, times);

                } catch (NumberFormatException err) {
                    System.out.println("Greska");
                }
            }
        });
    }
}