package bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import servent.SocketUtils;

public class BootstrapServerThread implements Runnable {

	private Socket sock;
	private Vector<String> users;

	public BootstrapServerThread(Socket sock, Vector<String> users) {

		this.sock = sock;
		this.users = users;

	}

	@Override
	public void run() {
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

			System.out.println("Korisnik " + sock.getInetAddress().getHostAddress() + " se konektovao");

			while (true) {

				String line = in.readLine();						//Server cita poruku koju je dobio
				
				String[] splitLine = line.split(" ");				//Splituje poruku
				//System.out.println(line);
				int otherPort = Integer.parseInt(splitLine[0]);		//Port na koji vraca poruku
				String message = splitLine[1];						//Poruka koju je dobio

				if (message.equals("novi")) {
					
					System.out.println("Novi cvor");
					Socket s = new Socket("127.0.0.1", otherPort);
					BufferedWriter w = new BufferedWriter(
							new OutputStreamWriter(
							s.getOutputStream()));
					System.out.println(otherPort + " prvi");
					w.write(otherPort + " prvi");					//Obavestavamo Servent da je prvi
					//w.write("\n");
					w.flush();
					s.close();
					break;

				} else {
					System.out.println("Nije dobro");
					//writer.write("Ova f-ja nije podrzana");
				}

			}
			
			in.close();
			writer.close();
			sock.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean postoji(String username) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).equals(username)) {
				return true; //potoji
			}
		}

		return false; //ne postoji - mozemo da dodamo
	}

}
