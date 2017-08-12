package bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Vector;



public class BootstrapServerThread implements Runnable {

	private Socket sock;
	private Vector<String> portovi;
	private static int prvi = 1;

	public BootstrapServerThread(Socket sock, Vector<String> portovi) {
		this.sock = sock;
		this.portovi = portovi;
	}

	@Override
	public void run() {
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(
							sock.getOutputStream()));

			System.out.println("Korisnik "
					+ sock.getInetAddress().getHostAddress() + " se konektovao");

			while (true) {

				String line = in.readLine();						//Server cita poruku koju je dobio

				String[] splitLine = line.split(" ");				//Splituje poruku
				//System.out.println(line);
				int otherPort = Integer.parseInt(splitLine[0]);		//Port na koji vraca poruku
				String message = splitLine[1];						//Poruka koju je dobio

				if (message.equals("novi")) {
					if(prvi == 1) {
						System.out.println("Novi cvor");
						Socket s = new Socket("127.0.0.1", otherPort);
						BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
						System.out.println(otherPort + " prvi");

						w.write(otherPort + " prvi");					//Obavestavamo Servent da je prvi

						prvi = 0;										//Vise nema prvih cvorova

						portovi.add(sock.getInetAddress().getHostAddress() + ";" + Integer.toString(otherPort));	//Dodajemo u nas niz IP adrese i portove cvorova

						w.flush();
						s.close();
						System.out.println(portovi);					//Ispisujemo IP adresu i portove svih cvorova

						break;
					}else {
						System.out.println("Ostali cvor");
						Socket s = new Socket("127.0.0.1", otherPort);
						BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

						Random rand = new Random();
						String vratiPort;
						vratiPort = portovi.get(rand.nextInt(portovi.size()));
						System.out.println(vratiPort + " ostali");
						w.write(vratiPort + " ostali");

						portovi.add(sock.getInetAddress().getHostAddress() + ";" + Integer.toString(otherPort));

						w.flush();
						s.close();
						System.out.println(portovi);					//Ispisujemo IP adresu i portove svih cvorova
					}


				} else {
					System.out.println("Nije dobro");
				}





			}

			in.close();
			writer.close();
			sock.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
