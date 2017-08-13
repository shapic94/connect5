package bootstrap;

import graph.GraphSingleton;
import storage.Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

public class BootstrapServerThread implements Runnable {

	private Socket sock;
	private Vector<String> nodes;
	private static int prvi = 1;

	public BootstrapServerThread(Socket sock, Vector<String> nodes) {
		this.sock = sock;
		this.nodes = nodes;
	}

	@Override
	public void run() {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

			System.out.println("Korisnik " + sock.getInetAddress().getHostAddress() + " se konektovao");

			while (true) {

				// Read message
				String line = reader.readLine();

				// Split message
				String[] message = line.split(" ");

				// Key
				String key = message[0];
				int nodePort = Integer.parseInt(message[1]);


				// Only New Node call Bootstrap Server
				if (key.equals(Storage.NEW)) {

					// If he is first one
					if (prvi == 1) {
						System.out.println("Novi cvor");
						Socket s = new Socket(sock.getInetAddress().getHostAddress(), nodePort);
						BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

						// Create and send IP to FIRST NODE
						String nodeAddress = sock.getInetAddress().getHostAddress() + ":" + Integer.toString(nodePort);
						w.write(Storage.FIRST + " " + nodeAddress);
						System.out.println(Storage.FIRST + " " + nodeAddress);

						// First is done
						prvi = 0;

						// Dodajemo u nas niz IP adrese i portove cvorova
						nodes.add(nodeAddress);

						w.flush();
						s.close();

						// Ispisujemo IP adresu i portove svih cvorova
						System.out.println(nodes);

						break;
					} else {
						System.out.println("Ostali cvor");
						Socket s = new Socket(sock.getInetAddress().getHostAddress(), nodePort);
						BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

						Random rand = new Random();
						String randNodeAddress = nodes.get(rand.nextInt(nodes.size()));

//						String[] ip = randNodeAddress.split(";");
//						String randNodePort = ip[1];

						w.write(Storage.NOT_FIRST + " " + randNodeAddress);
						System.out.println(Storage.NOT_FIRST + " " + randNodeAddress);

						nodes.add(sock.getInetAddress().getHostAddress() + ":" + Integer.toString(nodePort));

						w.flush();
						s.close();

						// Ispisujemo IP adresu i portove svih cvorova
						System.out.println(nodes);
					}
				} else {
					System.out.println("Nije dobro");
				}
			}

			reader.close();
			writer.close();
			sock.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
