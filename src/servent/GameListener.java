package servent;

import connect5.Operations;
import global.Methods;
import global.Storage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

public class GameListener implements Runnable {

    private Socket socket;

    public void respond() {
        Thread gameThread = new Thread(this);

        gameThread.start();

    }

    public void run() {

        int[] object = new Operations().init(
                ServentSingleton.getInstance().getPlayer1(),
                ServentSingleton.getInstance().getPlayer2(),
                ServentSingleton.getInstance().getRow(),
                ServentSingleton.getInstance().getCol(),
                ServentSingleton.getInstance().getTokens(),
                ServentSingleton.getInstance().getTempTimes()
        );

//        ServentSingleton.getInstance().setResultPlayer1(ServentSingleton.getInstance().getResultPlayer1() + object[0]);
//        ServentSingleton.getInstance().setResultPlayer2(ServentSingleton.getInstance().getResultPlayer2() + object[1]);
//
////        System.out.println(ServentSingleton.getInstance().getPlayer1() + " : " + ServentSingleton.getInstance().getResultPlayer1());
////        System.out.println(ServentSingleton.getInstance().getPlayer2() + " : " + ServentSingleton.getInstance().getResultPlayer2());
//
//
//        String[] notifyAddress;
//
//            Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry) it.next();
//
//                // ne javljaj samom sebi
//                if (!pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
//
//                    // javi childovima
//                    if (Methods.isNode1(pair.getKey().toString()) || Methods.isNode2(pair.getKey().toString())) {
//                        notifyAddress = pair.getValue().toString().split(":");
//
//                        try {
//                            socket = new Socket(notifyAddress[0], Integer.parseInt(notifyAddress[1]));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        SocketUtils.writeLine(
//                                socket,
//                                Storage.NOTIFY_ALL + " " +
//                                        Storage.WIN_CHILD + " " +
//                                        socket.getInetAddress().getHostAddress() + ":" +
//                                        ServentListener.LISTENER_PORT + " " +
//                                        ServentSingleton.getInstance().getId() + " " +
//                                        ServentSingleton.getInstance().getIzigravanje() + " " +
//                                        object[0] + ":" + object[1]
//                        );
//
////                        System.out.println("pozovi child! " + Integer.parseInt(notifyAddress[1]));
//                    } else if ((Methods.isNode1(ServentSingleton.getInstance().getId()) || Methods.isNode2(ServentSingleton.getInstance().getId()))) {
//                        if (pair.getKey().toString().equals(Methods.getLocalParent(ServentSingleton.getInstance().getId()))) {
//                            //javi parentu
//
//                            if (pair.getValue().toString().contains(" ")) {
//                                notifyAddress = pair.getValue().toString().split(" ")[0].split(":");
//                            } else {
//                                notifyAddress = pair.getValue().toString().split(":");
//                            }
//
//                            try {
//                                socket = new Socket(notifyAddress[0], Integer.parseInt(notifyAddress[1]));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            SocketUtils.writeLine(
//                                    socket,
//                                    Storage.NOTIFY_ALL + " " +
//                                            Storage.WIN + " " +
//                                            socket.getInetAddress().getHostAddress() + ":" +
//                                            ServentListener.LISTENER_PORT + " " +
//                                            ServentSingleton.getInstance().getId() + " " +
//                                            ServentSingleton.getInstance().getIzigravanje() + " " +
//                                            object[0] + ":" + object[1]
//                            );
//
////                            System.out.println("pozovi parent! " + Integer.parseInt(notifyAddress[1]));
//                        }
//                    } else {
//                        //javi parentu
//
//                        if (pair.getValue().toString().contains(" ")) {
//                            notifyAddress = pair.getValue().toString().split(" ")[0].split(":");
//                        } else {
//                            notifyAddress = pair.getValue().toString().split(":");
//                        }
//
//                        try {
//                            socket = new Socket(notifyAddress[0], Integer.parseInt(notifyAddress[1]));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        SocketUtils.writeLine(
//                                socket,
//                                Storage.NOTIFY_ALL + " " +
//                                        Storage.WIN + " " +
//                                        socket.getInetAddress().getHostAddress() + ":" +
//                                        ServentListener.LISTENER_PORT + " " +
//                                        ServentSingleton.getInstance().getId() + " " +
//                                        ServentSingleton.getInstance().getIzigravanje() + " " +
//                                        object[0] + ":" + object[1]
//                        );
//
//                        System.out.println("pozovi parent! " + Integer.parseInt(notifyAddress[1]));
//                    }
//                }
//            }
//        }


    }
}
