package servent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class StartServent {
	private int sPort;
	
	
	public static void main(String [] args) throws Exception{
		Properties prop = new Properties();
		Scanner scan = new Scanner(System.in);
		FileInputStream fis;
		int novi = 1;
		int portCvora;
//		int portS = Integer.parseInt(args[0]);
		
		try {
			fis = new FileInputStream("src/servent.properties");				//citamo iz fajla portove
			prop.load(fis);
			
			
			int myPort = Integer.parseInt(prop.getProperty("myport"));			//Uzimamo port za serventListener
			//int myPort = 1234;
			myPort +=5;
			ServentListener.LISTENER_PORT = myPort;
			ServentListener listener = new ServentListener();
			listener.startListener();											//Kreiramo Servent listener koji slusa na odredjenom portu (Ceka da se neki servent zakaci na njega)
			
			
			System.out.println("ServerListener: " + myPort);
			boolean doPing = Boolean.parseBoolean(prop.getProperty("doping"));
			
			if (doPing) {
				int otherPort = Integer.parseInt(prop.getProperty("otherport"));
				
				while(true) {													//Ceka da korisnik unese broj porta na koji ce da se salje poruka
					//System.out.println("Unesi port preko koga saljes ping: ");
					//portCvora = scan.nextInt();
					if(novi==1) {
						Socket s = new Socket("127.0.0.1", 1234);
						SocketUtils.writeLine(s, myPort + " novi");
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
