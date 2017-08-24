package servent;
import java.io.IOException;
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
}
