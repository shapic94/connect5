package servent;

import connect5.Operations;
import global.Methods;
import global.Storage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

public class InputListener implements Runnable {

    private Socket socket;

    public void respond() {
        Thread inputThread = new Thread(this);

        inputThread.start();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String option = scanner.nextLine();
            String[] parseOption = option.split(" ");

            if (parseOption[0].toUpperCase().equals("START")) {

                ServentSingleton.getInstance().setResultPlayer1(0);
                ServentSingleton.getInstance().setResultPlayer2(0);

                Date date = new Date();
                String timestamp = new Timestamp(date.getTime()).toString();
                ServentSingleton.getInstance().setIzigravanje(Methods.getHash(timestamp));



                String player1 = parseOption[1];
                String player2 = parseOption[2];
                int row = Integer.parseInt(parseOption[3]);
                int col = Integer.parseInt(parseOption[4]);
                int tokens = Integer.parseInt(parseOption[5]);
                int times = Integer.parseInt(parseOption[6]);

                if (!player1.equals("") && !player2.equals("") && row != 0 && col != 0 && tokens != 0 && times != 0) {
                    String[] currentAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
                    try {
                        socket = new Socket(currentAddress[0], Integer.parseInt(currentAddress[1]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SocketUtils.writeLine(
                            socket,
                            Storage.NOTIFY_GLOBAL_PARENT + " " +
                                    Storage.GAME + " " +
                                    socket.getInetAddress().getHostAddress() + ":" +
                                    ServentListener.LISTENER_PORT + " " +
                                    player1 + ":" + player2 + ":" + row + ":" + col + ":" + tokens + ":" + times + " " +
                                    ServentSingleton.getInstance().getIzigravanje()
                    );
                }
            } else if (parseOption[0].toUpperCase().equals("RESULT")) {

                System.out.println("[GLOBAL] " + ServentSingleton.getInstance().getPlayer1() + " : " + ServentSingleton.getInstance().getResultPlayer1());
                System.out.println("[GLOBAL] " + ServentSingleton.getInstance().getPlayer2() + " : " + ServentSingleton.getInstance().getResultPlayer2());
                System.out.println("[LOCAL] " + ServentSingleton.getInstance().getPlayer1() + " : " + ServentSingleton.getInstance().getLocalResultPlayer1());
                System.out.println("[LOCAL] " + ServentSingleton.getInstance().getPlayer2() + " : " + ServentSingleton.getInstance().getLocalResultPlayer2());
            } else if (parseOption[0].toUpperCase().equals("CLEAR")) {
                for (int i = 0; i < 20; i++) {
                    System.out.println("-");
                }

                // Show id
                System.out.println("Id : " + ServentSingleton.getInstance().getId());

                // Show map
                System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

                System.out.println("--------------------------------------------------");
            }

            scanner = new Scanner(System.in);
        }


    }
}
