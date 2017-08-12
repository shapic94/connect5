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
		//System.out.println(line);
		String otherPort = splitLine[0];
		String message = splitLine[1];

		try {
			switch(message) {

				case "prvi":
					System.out.println("Bootstrap kaze da sam prvi");
					break;
				case "ostali":
					System.out.println("Bootstrap kaze da se javim " + otherPort);					//U ovom delu, CVOR koji se javio bootstrapu, dobija informaciju kome treba da se javi
					String[] splitLine1 = otherPort.split(";");
					String serventPort = splitLine1[1];
					Socket serventSocket = new Socket("localhost", Integer.parseInt(serventPort));
					SocketUtils.writeLine(serventSocket, ServentListener.LISTENER_PORT + " novi");
					break;
				case "novi":
					System.out.println("Javio mi se novi cvor " + otherPort);						//Ovde je neki CVOR obavesten da mu se javio novi cvor, i salje mu informaciju da treba kod njega da se poveze

					Socket serventSocket2 = new Socket("localhost", Integer.parseInt(otherPort));
					SocketUtils.writeLine(serventSocket2, ServentListener.LISTENER_PORT + " prihvacen");
					break;
				case "prihvacen":																	//Ovde je cvor obavesten da moze da se poveze
					System.out.println("Prihvacen sam");
					break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
