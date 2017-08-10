package main;

import connect5.Algorithm;
import connect5.Operations;
import entity.Token;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by nebojsa.sapic on 8/8/17.
 *
 * Black = 1
 * White = 2
 */
public class Main {

    public static void main(String [] args) {
        System.out.println("Welcome to connect5 game");

        // Rows
        int row = 10;

        // Columns
        int col = 10;

        // Tokens
        int tokens = 5;

        // Times
        int times = 1;

//        // Initializing
//        Object[] object = new Operations().init(row, col, tokens, times);

//        // Cast Algorithm from object
//        Algorithm algorithm = (Algorithm) object[0];
//
//        // Cast Tokens from object
//        Token black = (Token) object[1];
//        Token white = (Token) object[2];


        JFrame frame = new JFrame("Connect5");
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);



        JLabel rowL = new JLabel("Number of rows : ");
        JTextField rowTF = new JTextField(50);

        JLabel colL = new JLabel("Number of columns : ");
        JTextField colTF = new JTextField(50);

        JLabel tokensL = new JLabel("Number of tokens : ");
        JTextField tokensTF = new JTextField(50);

        JLabel timesL = new JLabel("Number of times : ");
        JTextField timesTF = new JTextField(50);

        JButton startB = new JButton("Start");

        panel.add(rowL);
        panel.add(rowTF);

        panel.add(colL);
        panel.add(colTF);

        panel.add(tokensL);
        panel.add(tokensTF);

        panel.add(timesL);
        panel.add(timesTF);

        panel.add(startB);

        startB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int row = Integer.parseInt(rowTF.getText());
                    int col = Integer.parseInt(colTF.getText());
                    int tokens = Integer.parseInt(tokensTF.getText());
                    int times = Integer.parseInt(timesTF.getText());


                    // Initializing
                    Object[] object = new Operations().init(row, col, tokens, times);

                } catch (NumberFormatException err) {
                    System.out.println("Greska");
                }
            }
        });

        frame.setVisible(true);
    }
}