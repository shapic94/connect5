package bootstrap;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class BootstrapServer {

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(1234);
			System.out.println("BootstrapServer radi!!!");

			Vector<String> portovi = new Vector<String>();

			while(true){
				Socket sock = ss.accept();
				new Thread(new BootstrapServerThread(sock, portovi)).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}