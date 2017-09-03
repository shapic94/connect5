package servent;
import global.Storage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServentListener implements Runnable {

	public static int LISTENER_PORT;

	public ServentListener() {
	}

	public void startListener() {
		Thread listenerThread = new Thread(this);

		listenerThread.start();


	}

	@Override
	public void run() {
		ServerSocket listenerSocket;
		try {
			listenerSocket = new ServerSocket(LISTENER_PORT);


			while (true) {
				Socket clientSocket = listenerSocket.accept();

				Responder r = new Responder(clientSocket);
				r.respond();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPortInUse(int port) {
		// Assume no connection is possible.
		boolean result = false;

		try {
			(new ServerSocket(port)).close();
			result = true;
		}
		catch(IOException e) {
			// Could not connect.
		}

		return result;
	}

	public static boolean isDead(String ip, int port) {
		// Assume no connection is possible.
		boolean result = true;

		try {
			(new Socket(ip, port)).close();
			result = false;
		} catch(IOException e) {
			// Could not connect.
		}

		return result;
	}

	public static void createSocket(String ip, String port, String info) {
		boolean cantConnect = false;
		boolean breakDead = true;
		while (breakDead) {
			if (!ServentListener.isDead(ip, Integer.parseInt(port))) {
				while (true) {
					try {
						Socket socket = new Socket(ip, Integer.parseInt(port));
						SocketUtils.writeLine(socket, info);

						if (cantConnect) {
							System.out.println("Connect to : " + ip + ":" + port);
						}
						breakDead = false;
						break;
					} catch (SocketException e) {
						System.out.println("SocketException : " + e.getMessage() + " - " + e.getCause() + " ------ " + e.getStackTrace());
					} catch (IOException e) {
						System.out.println("IOException : " + e.getMessage() + " - " + e.getCause() + " ------ " + e.getStackTrace());
					}
				}
			} else {
				cantConnect = true;
				System.out.println("Can't connect to : " + ip + ":" + port);
			}
		}
	}
}
