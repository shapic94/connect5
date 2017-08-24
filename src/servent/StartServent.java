package servent;
import global.Storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class StartServent {
	private int sPort;

	public static void main(String [] args) throws Exception{
		Properties prop = new Properties();

		FileInputStream fis;
		int novi = 1;

		try {
			// citamo iz fajla portove
			fis = new FileInputStream("src/servent.properties");
			prop.load(fis);

			// Uzimamo port za serventListener
			int myPort = Integer.parseInt(prop.getProperty("myport"));

			// Ovaj broj menjas svaki put kada pokreces novi servent(Znaci promenis, sejvujes i pokrenes)
//			myPort += 1; // 8126
			while (true) {
				if (ServentListener.isPortInUse(myPort)) {
					break;
				}
				myPort += 1;
			}


			ServentListener.LISTENER_PORT = myPort;
			ServentListener listener = new ServentListener();

			// Kreiramo Servent listener koji slusa na odredjenom portu (Ceka da se neki servent zakaci na njega)
			listener.startListener();

			System.out.println("ServerListener: " + myPort);
			boolean doPing = Boolean.parseBoolean(prop.getProperty("doping"));

			if (doPing) {
				int otherPort = Integer.parseInt(prop.getProperty("otherport"));

				//Ceka da korisnik unese broj porta na koji ce da se salje poruka
				while(true) {
					//System.out.println("Unesi port preko koga saljes ping: ");
					//portCvora = scan.nextInt();
					if(novi==1) {
						Socket s = new Socket(Storage.BOOTSTRAP_IP, Storage.BOOTSTRAP_PORT);
						SocketUtils.writeLine(s, Storage.NEW + " " + s.getInetAddress().getHostAddress() + ":" + myPort);
						System.out.println(Storage.NEW + " " + myPort);
						novi = 0;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
