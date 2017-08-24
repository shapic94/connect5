package bootstrap;
import global.Storage;
import main.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class BootstrapServer {

	public static void main(String[] args) {

		try {
			// Create socket with
			ServerSocket ss = new ServerSocket(Storage.BOOTSTRAP_PORT);

			// Bootstrap is working
			System.out.println("Server started on port " + Storage.BOOTSTRAP_PORT);

			// Create vector for all live nodes
			Vector<String> nodes = new Vector<String>();

			while(true){
				Socket sock = ss.accept();
				new Thread(new BootstrapServerThread(sock, nodes)).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}