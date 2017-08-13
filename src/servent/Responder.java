package servent;
import global.Methods;
import graph.Graph;
import graph.GraphSingleton;
import global.Storage;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Responder implements Runnable{

	private Socket clientSocket;
	private String[] ip;
	private String address;
	private int port;
	String wrap;
	HashMap<Integer, String> map;
	Socket serventSocket;


	public Responder(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void respond() {
		Thread responderThread = new Thread(this);

		responderThread.start();
	}

	@Override
	public void run() {
		// Read line
		String line = SocketUtils.readLine(clientSocket);

		// Split line
		String[] message = line.split(" ");
		String key = message[0];
		String nodeAddress = message[1];

		try {
			switch(key) {
				case "FIRST":
					System.out.println("Bootstrap kaze da sam prvi");

					ip = nodeAddress.split(":");
					address = ip[0];
					port = Integer.parseInt(ip[1]);

					// Nebojsa

					// Set Local Level
					wrap = "0," + address + "," + port;
					map = new HashMap<Integer, String>();
					map.put(0, wrap);
					ServentSingleton.getInstance().setList(map);

					// Set Triangle Level
					GraphSingleton.getInstance().addVertex("0");

					System.out.println(GraphSingleton.getInstance());

					break;
				case "NOT_FIRST":
					// U ovom delu, CVOR koji se javio bootstrapu, dobija informaciju kome treba da se javi
					System.out.println("Bootstrap kaze da se javim " + nodeAddress);

					ip = nodeAddress.split(":");
					address = ip[0];
					port = Integer.parseInt(ip[1]);

					serventSocket = new Socket(address, port);
					SocketUtils.writeLine(serventSocket, Storage.NEW_INFO + " " + serventSocket.getInetAddress().getHostAddress() + ":" + ServentListener.LISTENER_PORT);

					// If not first, call rand node
					// TODO: 8/13/17

					break;
				case "NEW_INFO":
					// Ovde je neki CVOR obavesten da mu se javio novi cvor, i salje mu informaciju da treba kod njega da se poveze
					System.out.println("Javio mi se novi cvor " + nodeAddress);

					ip = nodeAddress.split(":");
					address = ip[0];
					port = Integer.parseInt(ip[1]);

					serventSocket = new Socket(address, port);

					// Nebojsa

					// Get current nodeId
					String nodeId = Methods.parseLocalLevel(ServentSingleton.getInstance().getList().get(0))[0];


					// Ako se javio parentu
					if (Methods.isParentVertex(nodeId)) {

						// Ako parent ima prazno mesto za child
						if (Methods.hasEmptyField(Methods.parseVertices(GraphSingleton.getInstance().getVertices().toString()))) {
							System.out.println("usao1");
							// TODO: 8/13/17 STAVI GA NA MESTO, POPUNI LOKALNI INFO, POSALJI MU INFO

							String emptyField = Methods.getEmptyField(Methods.parseVertices(GraphSingleton.getInstance().getVertices().toString()));

							// Set Local Level
							wrap = emptyField + "," + address + "," + port;
							ServentSingleton.getInstance().updateList(Integer.parseInt(emptyField), wrap);

							// Set Triangle Level
							GraphSingleton.getInstance().addVertex(emptyField);
							GraphSingleton.getInstance().addEdge(nodeId, emptyField);

							HashMap<Integer, String> map1 = new HashMap<Integer,String>(ServentSingleton.getInstance().getList());
							map1.put(Integer.parseInt(emptyField), ServentSingleton.getInstance().getList().get(0).toString());
							map1.put(0, wrap);

							SocketUtils.writeLine(serventSocket, Storage.NEW_ACCEPT + " " + map1.toString().replace(" ", "--"));
							System.out.println(GraphSingleton.getInstance());
						} else if (Methods.hasParent(nodeId)) {
							System.out.println("usao2");
							// Ako ima parenta
							// TODO: 8/13/17  UZMI PARENT INFO
						} else {
							System.out.println("usao3");
						}
					} else {
						System.out.println("usao4");
						// Ako se nije javio parentu, vrati parenta
						String ip = "";
						String port = "";

						System.out.println(ServentSingleton.getInstance().getList().toString());
						String[] localAddress = Methods.parseHashMapOriginal(ServentSingleton.getInstance().getList().toString());
						for (int i = 0; i < ServentSingleton.getInstance().getList().size(); i++) {
							String[] parseHashMapLocalAgain = localAddress[i].split("=");
							String[] parseHashMapLocalAgain1;
							parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");

							System.out.println(parseHashMapLocalAgain[0] + " " + parseHashMapLocalAgain[1]);
							System.out.println(parseHashMapLocalAgain1[0] + " " + parseHashMapLocalAgain1[1] + " " + parseHashMapLocalAgain1[2]);
							if (parseHashMapLocalAgain1[0].equals("0")) {
								ip = parseHashMapLocalAgain1[1];
								port = parseHashMapLocalAgain1[2];
							}
						}

						SocketUtils.writeLine(serventSocket, Storage.NOT_FIRST + " " + ip + ":" + port);
						System.out.println(GraphSingleton.getInstance());
					}

					break;
				case "NEW_ACCEPT":
					//Ovde je cvor obavesten da moze da se poveze
					System.out.println("NEW_ACCPET");

					boolean callSocket = false;
					HashMap<Integer, String> map1 = new HashMap<Integer,String>(ServentSingleton.getInstance().getList());
					String currentVertex = "";

					String[] parseHashMapLocal = Methods.parseHashMap(nodeAddress);
					for (int i = 0; i < parseHashMapLocal.length; i++ ) {
						String[] parseHashMapLocalAgain = parseHashMapLocal[i].split("=");
						ServentSingleton.getInstance().getList().put(Integer.parseInt(parseHashMapLocalAgain[0]), parseHashMapLocalAgain[1]);

						String[] parseHashMapLocalAgain1;
						parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");

						// Set Triangle Level
						GraphSingleton.getInstance().addVertex(parseHashMapLocalAgain1[0]);

						// Ako nije trenutni
						if (!parseHashMapLocalAgain[0].equals("0")) {
							GraphSingleton.getInstance().addEdge(currentVertex, parseHashMapLocalAgain1[0]);

							// Ako nije parent
							if(!parseHashMapLocalAgain1[0].equals("0")) {
								address = parseHashMapLocalAgain1[1];
								port = Integer.parseInt(parseHashMapLocalAgain1[2]);

								// Set Local Level
								String temp = ServentSingleton.getInstance().getList().get(Integer.parseInt(parseHashMapLocalAgain[0])).toString();
								map1.put(Integer.parseInt(parseHashMapLocalAgain[0]), ServentSingleton.getInstance().getList().get(0).toString());
								map1.put(0, temp);

								callSocket = true;
							} else {
								map1.put(Integer.parseInt(parseHashMapLocalAgain[0]), parseHashMapLocalAgain[1]);
							}
						} else {
							currentVertex = parseHashMapLocalAgain1[0];
						}


					}

					if(callSocket) {
						// Ako sam drugi child, obavesti prvog
						serventSocket = new Socket(address, port);
						SocketUtils.writeLine(serventSocket, Storage.NEW_ARRIVED + " " + map1.toString().replace(" ", "--"));

					}

					System.out.println(ServentSingleton.getInstance().getList().toString());
					System.out.println(GraphSingleton.getInstance());
					break;
				case "NEW_ARRIVED":
					System.out.println("NEW_ARRIVED");
					System.out.println(nodeAddress);

					String[] parseHashMapLocalArrived = Methods.parseHashMap(nodeAddress);
					String currentVertex1 = "";
					HashMap<Integer, String> map2 = new HashMap<Integer,String>(ServentSingleton.getInstance().getList());
					for (int i = 0; i < parseHashMapLocalArrived.length; i++ ) {
						String[] parseHashMapLocalAgain = parseHashMapLocalArrived[i].split("=");
						ServentSingleton.getInstance().getList().put(Integer.parseInt(parseHashMapLocalAgain[0]), parseHashMapLocalAgain[1]);

						String[] parseHashMapLocalAgain1;
						parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");

						// Set Triangle Level
						GraphSingleton.getInstance().addVertex(parseHashMapLocalAgain1[0]);

						// Ako nije trenutni
						if (!parseHashMapLocalAgain[0].equals("0")) {
							GraphSingleton.getInstance().addEdge(currentVertex1, parseHashMapLocalAgain1[0]);
						} else {
							currentVertex1 = parseHashMapLocalAgain1[0];
						}
					}

					System.out.println(ServentSingleton.getInstance().getList().toString());
					System.out.println(GraphSingleton.getInstance());
					break;
				default:
					System.out.println("Wrong communication.");
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
