package servent;
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
		String line = SocketUtils.readLine(clientSocket);
		
		String[] splitLine = line.split(" ");								//Client splituje poruku koju je dobio i u zavisnosti od poruke radi zadatak
		System.out.println(line);
		int otherPort = Integer.parseInt(splitLine[0]);
		String message = splitLine[1];
		
		try {
			switch(message) {
			case "ping":
				Socket otherSocket = new Socket("localhost", otherPort);
				SocketUtils.writeLine(otherSocket, ServentListener.LISTENER_PORT + " pong");	
				break;
			case "pong":
				System.out.println("pong received");
				break;
			case "prvi":
				System.out.println("Bootstrap kaze da sam prvi");
				break;
			
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
