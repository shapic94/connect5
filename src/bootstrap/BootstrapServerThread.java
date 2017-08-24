package bootstrap;

import global.Storage;
import servent.Servent;
import servent.ServentListener;
import servent.SocketUtils;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

public class BootstrapServerThread implements Runnable {

	private Socket sock;
	private Socket s;
	private Vector<String> nodes;
	private static boolean prvi = true;
	String[] parseValue;
	Random rand;
	String randNodeAddress;
	private static boolean started = false;
	private static String player1 = "";
	private static String player2 = "";
	private static String row = "";
	private static String col = "";
	private static String tokens = "";
	private static String times = "";

	public BootstrapServerThread(Socket sock, Vector<String> nodes) {
		this.sock = sock;
		this.nodes = nodes;
	}

	@Override
	public void run() {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

			while (true) {

				// Read message
				String line = reader.readLine();

				// Split message
				String[] message = line.split(" ");

				// Key
				String key = message[0];
				String value = message[1];

				try {
					switch (key) {
						case "NEW":

							// Parse
							parseValue = value.split(":");
							String ip = parseValue[0];
							int nodePort = Integer.parseInt(parseValue[1]);

							// Only New Node call Bootstrap Server
							if (key.equals(Storage.NEW)) {

								// If he is first one
								if (isPrvi()) {
									System.out.println("Prvi čvor " + sock.getInetAddress().getHostAddress() + " se konektovao sa porukom: " + line);

									// Create socket and writer
									s = new Socket(sock.getInetAddress().getHostAddress(), nodePort);
									BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

									// Create and send IP to FIRST NODE
									String nodeAddress = sock.getInetAddress().getHostAddress() + ":" + Integer.toString(nodePort);

									// if game started send GUI_INFO
									if (isStarted()) {
										nodeAddress += " " + getPlayer1() + "," +
												getPlayer2() + "," +
												getRow() + "," +
												getCol() + "," +
												getTokens() + "," +
												getTimes();
									}

									// Write
									w.write(
										Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.FIRST_NODE + " " +
										nodeAddress
									);

									System.out.println("SEND: " + Storage.NOTIFY_GLOBAL_PARENT + " " + Storage.NODE + " " + nodeAddress);

									// First is done
									setPrvi(false);

									// Save info
									nodes.add(nodeAddress);

									// Close
									w.flush();
									s.close();

									// Ispisujemo IP adresu i portove svih cvorova
									System.out.println("Trenutni čvorovi: " + nodes);

									break;
								} else {
									System.out.println("Čvor " + sock.getInetAddress().getHostAddress() + " se konektovao sa porukom: " + line);

									// Create socket and writer
									s = new Socket(sock.getInetAddress().getHostAddress(), nodePort);
									BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

									// Random addres from nodes
									while (true) {
										rand = new Random();
										int randomNode = rand.nextInt(nodes.size());
										randNodeAddress = nodes.get(randomNode); // nodePort = 8126; random =  8125

										// If new node equal port from vector
										if (String.valueOf(nodePort).equals(randNodeAddress.split(":")[1])) {
											nodes.remove(randNodeAddress);

											continue;
										}

										// If random node port is down
										if (ServentListener.isPortInUse(Integer.parseInt(randNodeAddress.split(":")[1]))) {
											nodes.remove(randNodeAddress);
											continue;
										}

										break;


//										if (String.valueOf(nodePort).equals(randNodeAddress.split(":")[1]) || ServentListener.isPortInUse(Integer.parseInt(randNodeAddress.split(":")[1]))) { // 8126 => true => false
//											System.out.println(randNodeAddress);
//											System.out.println(randomNode);
//											nodes.remove(randomNode);
//										} else {
//											System.out.println(randNodeAddress);
//											System.out.println(randomNode);
//											break;
//										}
									}
									// if game started send GUI_INFO
									if (isStarted()) {
										randNodeAddress += " " + getPlayer1() + "," +
												getPlayer2() + "," +
												getRow() + "," +
												getCol() + "," +
												getTokens() + "," +
												getTimes();
									}

									// Write
									w.write(
										Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.NODE + " " +
										randNodeAddress
									);

									System.out.println("SEND: " + Storage.NOT_FIRST + " " + randNodeAddress);

									// Save info
									if (!nodes.contains(sock.getInetAddress().getHostAddress() + ":" + Integer.toString(nodePort))) {
										nodes.add(sock.getInetAddress().getHostAddress() + ":" + Integer.toString(nodePort));
									}

									// Close
									w.flush();
									s.close();

									// Ispisujemo IP adresu i portove svih cvorova
									System.out.println("Trenutni čvorovi: " + nodes);
								}
							} else {
								System.out.println("Losa kljucna rec za slanje serveru.");
							}

							System.out.println("--------------------------------------------------");

							// Close
							reader.close();
							writer.close();
							sock.close();
							break;
						case "GUI_INFO":
							System.out.println("GUI " + sock.getInetAddress().getHostAddress() + " se konektovao sa porukom: " + line);

							// Parse
							parseValue = message[2].split(",");

							setPlayer1(parseValue[0]);
							setPlayer2(parseValue[1]);
							setRow(parseValue[2]);
							setCol(parseValue[3]);
							setTokens(parseValue[4]);
							setTimes(parseValue[5]);

							setStarted(true);

							if (!nodes.isEmpty()) {
								rand = new Random();
								randNodeAddress = nodes.get(rand.nextInt(nodes.size()));
								s = new Socket(randNodeAddress.split(":")[0], Integer.parseInt(randNodeAddress.split(":")[1]));
								randNodeAddress += " " + getPlayer1() + "," +
										getPlayer2() + "," +
										getRow() + "," +
										getCol() + "," +
										getTokens() + "," +
										getTimes();

								SocketUtils.writeLine(s, Storage.GUI_INFO + " " + randNodeAddress);
								System.out.println("SEND: " + Storage.GUI_INFO + " " + randNodeAddress);
							}
							break;
						default:
							break;

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static String getPlayer1() {
		return player1;
	}

	public static void setPlayer1(String player1) {
		BootstrapServerThread.player1 = player1;
	}

	public static String getPlayer2() {
		return player2;
	}

	public static void setPlayer2(String player2) {
		BootstrapServerThread.player2 = player2;
	}

	public static String getRow() {
		return row;
	}

	public static void setRow(String row) {
		BootstrapServerThread.row = row;
	}

	public static String getCol() {
		return col;
	}

	public static void setCol(String col) {
		BootstrapServerThread.col = col;
	}

	public static String getTokens() {
		return tokens;
	}

	public static void setTokens(String tokens) {
		BootstrapServerThread.tokens = tokens;
	}

	public static String getTimes() {
		return times;
	}

	public static void setTimes(String times) {
		BootstrapServerThread.times = times;
	}

	public static boolean isStarted() {
		return started;
	}

	public static void setStarted(boolean started) {
		BootstrapServerThread.started = started;
	}

	public static boolean isPrvi() {
		return prvi;
	}

	public static void setPrvi(boolean prvi) {
		BootstrapServerThread.prvi = prvi;
	}
}
