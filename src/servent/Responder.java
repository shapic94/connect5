package servent;
import graph.Graph;
import graph.GraphSingleton;
import storage.Storage;

import java.io.IOException;
import java.net.Socket;

public class Responder implements Runnable{

	private Socket clientSocket;

	public Responder(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void respond() {
		Thread responderThread = new Thread(this);

		responderThread.start();
	}

	@Override
	public void run() {
		// Read line
		String line = SocketUtils.readLine(clientSocket);

		// Split line
		String[] message = line.split(" ");
		String key = message[0];
		String nodePort = message[1];

		try {
			switch(key) {
				case "FIRST":
					System.out.println("Bootstrap kaze da sam prvi");

					// Nebojsa
					// If first, addVertex
					GraphSingleton.getInstance().addVertex("0");
					System.out.println(GraphSingleton.getInstance());

					break;
				case "NOT_FIRST":
					// U ovom delu, CVOR koji se javio bootstrapu, dobija informaciju kome treba da se javi
					System.out.println("Bootstrap kaze da se javim " + nodePort);

					Socket serventSocket = new Socket("localhost", Integer.parseInt(nodePort));
					SocketUtils.writeLine(serventSocket, Storage.NEW_INFO + " " + ServentListener.LISTENER_PORT);

					// If not first, call rand node
					// TODO: 8/13/17

					break;
				case "NEW_INFO":
					// Ovde je neki CVOR obavesten da mu se javio novi cvor, i salje mu informaciju da treba kod njega da se poveze
					System.out.println("Javio mi se novi cvor " + nodePort);

					Socket serventSocket2 = new Socket("localhost", Integer.parseInt(nodePort));
					SocketUtils.writeLine(serventSocket2, Storage.NEW_ACCEPT + " " + ServentListener.LISTENER_PORT);

					break;
				case "NEW_ACCEPT":
					//Ovde je cvor obavesten da moze da se poveze
					System.out.println("Prihvacen sam");
					break;
				default:
					System.out.println("Wrong communication.");
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
