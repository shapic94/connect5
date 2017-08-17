package servent;
import connect5.Operations;
import global.Methods;
import graph.Graph;
import graph.GraphSingleton;
import global.Storage;
import gui.Component;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
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
	String[] parseValue;
	String nodeId;

	Socket socket = null;


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
//		String[] message = line.split(" ");
//		String key = message[0];
//		String nodeAddress = message[1];

		// Split line
		String[] message = line.split(" ");
		String globalKey = message[0];
		String localKey = message[1];
		String address = message[2];

		String ip = address.split(":")[0];
		String port = address.split(":")[1];


		switch(globalKey) {
			case "NOTIFY_GLOBAL_PARENT":
				switch (localKey) {
					case "FIRST_NODE":

						// Set Servant
						ServentSingleton.getInstance().setId("0");
						ServentSingleton.getInstance().setEmptyLocalChild(2);
						ServentSingleton.getInstance().setEmptyGlobalChild(0);
						ServentSingleton.getInstance().updateList("0", ip + ":" + port);

						// Show id
						System.out.println("Id : " + ServentSingleton.getInstance().getId());

						// Show map
						System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

						System.out.println("--------------------------------------------------");
						break;
					case "NODE":

						// If it is created node
						if (ServentSingleton.getInstance().getId() != null) {

							// If it is global parent
							if (Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								// If has empty local child
								if (ServentSingleton.getInstance().getEmptyLocalChild() > 0) {

									String emptyChildId = Methods.getEmptyChildId(ServentSingleton.getInstance().getList());

									// Set Servant
									ServentSingleton.getInstance().setEmptyLocalChild(ServentSingleton.getInstance().getEmptyLocalChild() - 1);
									ServentSingleton.getInstance().updateList(emptyChildId, ip + ":" + port);

									// Create socket
									try {
										socket = new Socket(ip, Integer.parseInt(port));
									} catch (IOException e) {
										e.printStackTrace();
									}

									// Call accept child NOTIFY_CHILD ACCEPT_NODE ip:port map id
									SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_CHILD + " " +
										Storage.ACCEPT_NODE + " " +
										socket.getInetAddress().getHostAddress() + ":" +
										ServentListener.LISTENER_PORT + " " +
										ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
										emptyChildId
									);

									// Notify child
									if (ServentSingleton.getInstance().getList().containsKey("1") && emptyChildId != "1") {

										String[] addressChild = ServentSingleton.getInstance().getList().get("1").split(":");

										try {
											socket = new Socket(addressChild[0], Integer.parseInt(addressChild[1]));
										} catch (IOException e) {
											e.printStackTrace();
										}

										SocketUtils.writeLine(
											socket,
											Storage.NOTIFY_CHILD + " " +
											Storage.ACCEPT_NODE + " " +
											socket.getInetAddress().getHostAddress() + ":" +
											ServentListener.LISTENER_PORT + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--")
										);
									} else if (ServentSingleton.getInstance().getList().containsKey("2") && emptyChildId != "2") {

										String[] addressChild = ServentSingleton.getInstance().getList().get("2").split(":");

										try {
											socket = new Socket(addressChild[0], Integer.parseInt(addressChild[1]));
										} catch (IOException e) {
											e.printStackTrace();
										}

										SocketUtils.writeLine(
											socket,
											Storage.NOTIFY_CHILD + " " +
											Storage.ACCEPT_NODE + " " +
											socket.getInetAddress().getHostAddress() + ":" +
											ServentListener.LISTENER_PORT + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--")
										);

									}

									// Show id
									System.out.println("Id : " + ServentSingleton.getInstance().getId());

									// Show map
									System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

									System.out.println("--------------------------------------------------");

								} else if (ServentSingleton.getInstance().getEmptyGlobalChild() > 0) {
									// If has empty global child

								} else {
									// If not, create new parent

									String parentId = "1." + ServentSingleton.getInstance().getId();

									// Set Servant
									ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
									Methods.extendHashMap(ServentSingleton.getInstance().getList());
									ServentSingleton.getInstance().updateList(parentId, ip + ":" + port);

									// Create socket
									try {
										socket = new Socket(ip, Integer.parseInt(port));
									} catch (IOException e) {
										e.printStackTrace();
									}

									// Call accept child NOTIFY_CHILD ACCEPT_NODE ip:port map id

									// Prepare new map
									HashMap<String, String> parentHashmap = new HashMap<String, String>();
									parentHashmap.put(ServentSingleton.getInstance().getId(), ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()));
									SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
										Storage.ACCEPT_NODE + " " +
										socket.getInetAddress().getHostAddress() + ":" +
										ServentListener.LISTENER_PORT + " " +
										parentHashmap.toString().replace(" ", "--") + " " +
										parentId
									);

									Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
									while (it.hasNext()) {
										Map.Entry pair = (Map.Entry) it.next();

										String[] parseKey = pair.getKey().toString().split("\\.");
										if (parseKey[parseKey.length - 1].equals(Storage.NODE_1)) {
											String[] addressChild = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + Storage.NODE_1).split(":");

											try {
												socket = new Socket(addressChild[0], Integer.parseInt(addressChild[1]));
											} catch (IOException e) {
												e.printStackTrace();
											}

											SocketUtils.writeLine(
												socket,
												Storage.NOTIFY_CHILD + " " +
												Storage.ACCEPT_NODE + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--")
											);
										} else if (parseKey[parseKey.length - 1].equals(Storage.NODE_2)) {
											String[] addressChild = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + Storage.NODE_2).split(":");

											try {
												socket = new Socket(addressChild[0], Integer.parseInt(addressChild[1]));
											} catch (IOException e) {
												e.printStackTrace();
											}

											SocketUtils.writeLine(
												socket,
												Storage.NOTIFY_CHILD + " " +
												Storage.ACCEPT_NODE + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--")
											);
										}
									}

									// Show id
									System.out.println("Id : " + ServentSingleton.getInstance().getId());

									// Show map
									System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

									System.out.println("--------------------------------------------------");
								}
							} else {
								// Go to global parent
								String id = ServentSingleton.getInstance().getId();
								String test = ServentSingleton.getInstance().getList().get(id.substring(0, id.length() - 1) + "0");
								String[] addressParent = test.split(":");

								// Create socket
								try {
									socket = new Socket(ip, Integer.parseInt(port));
								} catch (IOException e) {
									e.printStackTrace();
								}

								// Call accept child NOTIFY_CHILD ACCEPT_NODE ip:port map id
								SocketUtils.writeLine(
									socket,
									Storage.NOTIFY_GLOBAL_PARENT + " " +
									Storage.NODE + " " +
									addressParent[0] + ":" +
									addressParent[1]
								);
							}
						} else {

							// Create socket
							try {
								socket = new Socket(ip, Integer.parseInt(port));
							} catch (IOException e) {
								e.printStackTrace();
							}

							// Call global parent
							SocketUtils.writeLine(
								socket,
								Storage.NOTIFY_GLOBAL_PARENT + " " +
								Storage.NODE + " " +
								socket.getInetAddress().getHostAddress() + ":" +
								ServentListener.LISTENER_PORT
							);
						}
						break;
					case "GAME":

						break;
					case "WIN":

						break;
				}
				break;
			case "NOTIFY_PARENT":
				switch (localKey) {
					case "NODE":
						// If it is local parent
						if (Methods.isLocalParent(ServentSingleton.getInstance().getId())) {

							// If has empty local child
							if (ServentSingleton.getInstance().getEmptyLocalChild() > 0) {

							} else if (ServentSingleton.getInstance().getEmptyGlobalChild() > 0) {
								// If has empty global child

							} else {
								// If not, create new parent

							}
						} else {
							// Go to local parent
						}

						break;
					case "ACCEPT_NODE":
						String map = message[3];
						String id = message[4];

						// Set Servant
						ServentSingleton.getInstance().setId(id);
						ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));
						ServentSingleton.getInstance().updateList(id, ip + ":" + ServentListener.LISTENER_PORT);

						// Show id
						System.out.println("Id : " + ServentSingleton.getInstance().getId());

						// Show map
						System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

						System.out.println("--------------------------------------------------");
						break;
				}
				break;
			case "NOTIFY_CHILD":
				switch (localKey) {
					case "ACCEPT_NODE":

						String map = message[3];

						// If accept new node
						if (message.length == 5) {
							String id = message[4];

							System.out.println(id);

							// Set Servant
							ServentSingleton.getInstance().setId(id);
							ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));

						} else {
							// If notify old node

							// Set Servant
							ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));
						}

						// Show id
						System.out.println("Id : " + ServentSingleton.getInstance().getId());

						// Show map
						System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

						System.out.println("--------------------------------------------------");
						break;
				}
				break;

//			case "FIRST":
//				System.out.println("Bootstrap kaze da sam prvi čvor");
//
//				// Parse string
////					ip = nodeAddress.split(":");
////					address = ip[0];
////					port = Integer.parseInt(ip[1]);
//
//				// Set Local Level
////					wrap = "0," + address + "," + port;
//				map = new HashMap<Integer, String>();
//				map.put(0, "0," + this.clientSocket.getInetAddress().getHostAddress() + "," + this.clientSocket.getPort());
//				System.out.println(map.get(0));
//				ServentSingleton.getInstance().setList(map);
//
//				// Set Triangle Level
//				GraphSingleton.getInstance().addVertex("0");
//				System.out.println(GraphSingleton.getInstance());
//
//				// Start game if info is here
//				if (message.length == 3) {
//					parseValue = message[2].split(",");
//					Object[] object = new Operations().init(parseValue[0], parseValue[1], Integer.parseInt(parseValue[2]), Integer.parseInt(parseValue[3]), Integer.parseInt(parseValue[4]), Integer.parseInt(parseValue[5]));
//				}
//				break;
//				case "NOT_FIRST":
//					// U ovom delu, CVOR koji se javio bootstrapu, dobija informaciju kome treba da se javi
//					System.out.println("Bootstrap kaze da se javim " + nodeAddress);
//
//					ip = nodeAddress.split(":");
//					address = ip[0];
//					port = Integer.parseInt(ip[1]);
//
//					serventSocket = new Socket(address, port);
//					SocketUtils.writeLine(serventSocket, Storage.NEW_INFO + " " + serventSocket.getInetAddress().getHostAddress() + ":" + ServentListener.LISTENER_PORT);
//
//					// If not first, call rand node
//					// TODO: 8/13/17
//
//					break;
//				case "GUI_INFO":
//
//					//
//					String[] GUI_INFO_localAddress;
//					String GUI_INFO_ip = "";
//					String GUI_INFO_port = "";
//
//					// Get current nodeId
//					nodeId = Methods.parseLocalLevel(ServentSingleton.getInstance().getList().get(0))[0];
//
//					// Ako se javio parentu
//					if (Methods.isParentVertex(nodeId)) {
//						// Start game if info is here
//						if (message.length == 3) {
//							parseValue = message[2].split(",");
//
//							Object[] object = new Operations().init(
//								parseValue[0],
//								parseValue[1],
//								Integer.parseInt(parseValue[2]),
//								Integer.parseInt(parseValue[3]),
//								Integer.parseInt(parseValue[4]),
//								(Integer.parseInt(parseValue[5]) / ServentSingleton.getInstance().getList().size())
//							);
//
//							GUI_INFO_localAddress = Methods.parseHashMapOriginal(ServentSingleton.getInstance().getList().toString());
//							for (int i = 0; i < ServentSingleton.getInstance().getList().size(); i++) {
//								String[] parseHashMapLocalAgain = GUI_INFO_localAddress[i].split("=");
//								String[] parseHashMapLocalAgain1;
//								parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");
//
//								if (!parseHashMapLocalAgain1[0].equals("0")) {
//									GUI_INFO_ip = parseHashMapLocalAgain1[1];
//									GUI_INFO_port = parseHashMapLocalAgain1[2];
//
//									serventSocket = new Socket(GUI_INFO_ip, Integer.parseInt(GUI_INFO_port));
//									SocketUtils.writeLine(
//										serventSocket,
//										Storage.PLAY + " " +
//										parseValue[0] + "," +
//										parseValue[1] + "," +
//										parseValue[2] + "," +
//										parseValue[3] + "," +
//										parseValue[4] + "," +
//										(Integer.parseInt(parseValue[5]) / ServentSingleton.getInstance().getList().size())
//									);
//								}
//							}
//						}
//					} else {
//						// Ako se nije javio parentu, vrati parenta
//						// TODO: 8/13/17  DONE
//
//						System.out.println("Pozovi parenta za igranje");
//
//						GUI_INFO_localAddress = Methods.parseHashMapOriginal(ServentSingleton.getInstance().getList().toString());
//						for (int i = 0; i < ServentSingleton.getInstance().getList().size(); i++) {
//							String[] parseHashMapLocalAgain = GUI_INFO_localAddress[i].split("=");
//							String[] parseHashMapLocalAgain1;
//							parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");
//
//							if (parseHashMapLocalAgain1[0].equals("0")) {
//								GUI_INFO_ip = parseHashMapLocalAgain1[1];
//								GUI_INFO_port = parseHashMapLocalAgain1[2];
//							}
//						}
//
//						if (message.length == 3) {
//							parseValue = message[2].split(",");
//							serventSocket = new Socket(GUI_INFO_ip, Integer.parseInt(GUI_INFO_port));
//							SocketUtils.writeLine(
//								serventSocket,
//								Storage.GUI_INFO + " " +
//								GUI_INFO_ip + ":" +
//								GUI_INFO_port + " " +
//								parseValue[0] + "," +
//								parseValue[1] + "," +
//								parseValue[2] + "," +
//								parseValue[3] + "," +
//								parseValue[4] + "," +
//								parseValue[5]
//							);
//						}
//					}
//
//					break;
//				case "NEW_INFO":
//					// Ovde je neki CVOR obavesten da mu se javio novi cvor, i salje mu informaciju da treba kod njega da se poveze
//					System.out.println("Javio mi se novi cvor " + nodeAddress);
//
//					ip = nodeAddress.split(":");
//					address = ip[0];
//					port = Integer.parseInt(ip[1]);
//
//					serventSocket = new Socket(address, port);
//
//					// Nebojsa
//
//					// Get current nodeId
//					String nodeId = Methods.parseLocalLevel(ServentSingleton.getInstance().getList().get(0))[0];
//
//					// Ako se javio parentu
//					if (Methods.isParentVertex(nodeId)) {
//
//						// Ako parent ima prazno mesto za child
//						// TODO: 8/13/17 DONE
//						if (Methods.hasEmptyField(Methods.parseVertices(GraphSingleton.getInstance().getVertices().toString()))) {
//							// -------- CHECK --------
//							System.out.println("usao1");
//							// -------- CHECK --------
//
//							String emptyField = Methods.getEmptyField(Methods.parseVertices(GraphSingleton.getInstance().getVertices().toString()));
//
//							// Set Local Level
//							wrap = emptyField + "," + address + "," + port;
//							ServentSingleton.getInstance().updateList(Integer.parseInt(emptyField), wrap);
//
//							// Set Triangle Level
//							GraphSingleton.getInstance().addVertex(emptyField);
//							GraphSingleton.getInstance().addEdge(nodeId, emptyField);
//
//							HashMap<Integer, String> map1 = new HashMap<Integer,String>(ServentSingleton.getInstance().getList());
//							map1.put(Integer.parseInt(emptyField), ServentSingleton.getInstance().getList().get(0).toString());
//							map1.put(0, wrap);
//
//							SocketUtils.writeLine(serventSocket, Storage.NEW_ACCEPT + " " + map1.toString().replace(" ", "--"));
//							System.out.println(GraphSingleton.getInstance());
//						} else if (Methods.hasParent(nodeId)) {
//							// Ako ima parenta
//							// TODO: 8/13/17  UZMI PARENT INFO
//							// -------- CHECK --------
//							System.out.println("usao2");
//							// -------- CHECK --------
//						} else if (Methods.hasParentChild()) {
//							// Ako ima childa koji je nekome parent
//							// TODO: 8/14/17
//							// -------- CHECK --------
//							System.out.println("usao3");
//							// -------- CHECK --------
//						} else {
//							// Ako nema gde, pravi novi trougao
//							// TODO: 8/14/17
//							// -------- CHECK --------
//							System.out.println("usao4");
//							// -------- CHECK --------
//						}
//					} else {
//						// Ako se nije javio parentu, vrati parenta
//						// TODO: 8/13/17  DONE
//						// -------- CHECK --------
//						System.out.println("usao5");
//						// -------- CHECK --------
//
//						String ip = "";
//						String port = "";
//
//						System.out.println(ServentSingleton.getInstance().getList().toString());
//						String[] localAddress = Methods.parseHashMapOriginal(ServentSingleton.getInstance().getList().toString());
//						for (int i = 0; i < ServentSingleton.getInstance().getList().size(); i++) {
//							String[] parseHashMapLocalAgain = localAddress[i].split("=");
//							String[] parseHashMapLocalAgain1;
//							parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");
//
//							System.out.println(parseHashMapLocalAgain[0] + " " + parseHashMapLocalAgain[1]);
//							System.out.println(parseHashMapLocalAgain1[0] + " " + parseHashMapLocalAgain1[1] + " " + parseHashMapLocalAgain1[2]);
//							if (parseHashMapLocalAgain1[0].equals("0")) {
//								ip = parseHashMapLocalAgain1[1];
//								port = parseHashMapLocalAgain1[2];
//							}
//						}
//
//						SocketUtils.writeLine(serventSocket, Storage.NOT_FIRST + " " + ip + ":" + port);
//						System.out.println(GraphSingleton.getInstance());
//					}
//
//					break;
//				case "NEW_ACCEPT":
//					// Ovde je cvor obavesten da moze da se poveze
//					System.out.println("NEW_ACCPET");
//
//					boolean callSocket = false;
//					HashMap<Integer, String> map1 = new HashMap<Integer,String>(ServentSingleton.getInstance().getList());
//					String currentVertex = "";
//
//					String[] parseHashMapLocal = Methods.parseHashMap(nodeAddress);
//					for (int i = 0; i < parseHashMapLocal.length; i++ ) {
//						String[] parseHashMapLocalAgain = parseHashMapLocal[i].split("=");
//						ServentSingleton.getInstance().getList().put(Integer.parseInt(parseHashMapLocalAgain[0]), parseHashMapLocalAgain[1]);
//
//						String[] parseHashMapLocalAgain1;
//						parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");
//
//						// Set Triangle Level
//						GraphSingleton.getInstance().addVertex(parseHashMapLocalAgain1[0]);
//
//						// Ako nije trenutni
//						if (!parseHashMapLocalAgain[0].equals("0")) {
//							GraphSingleton.getInstance().addEdge(currentVertex, parseHashMapLocalAgain1[0]);
//
//							// Ako nije parent
//							if(!parseHashMapLocalAgain1[0].equals("0")) {
//								address = parseHashMapLocalAgain1[1];
//								port = Integer.parseInt(parseHashMapLocalAgain1[2]);
//
//								// Set Local Level
//								String temp = ServentSingleton.getInstance().getList().get(Integer.parseInt(parseHashMapLocalAgain[0])).toString();
//								map1.put(Integer.parseInt(parseHashMapLocalAgain[0]), ServentSingleton.getInstance().getList().get(0).toString());
//								map1.put(0, temp);
//
//								callSocket = true;
//							} else {
//								map1.put(Integer.parseInt(parseHashMapLocalAgain[0]), parseHashMapLocalAgain[1]);
//							}
//						} else {
//							currentVertex = parseHashMapLocalAgain1[0];
//						}
//
//
//					}
//
//					if(callSocket) {
//						// Ako sam drugi child, obavesti prvog
//						serventSocket = new Socket(address, port);
//						SocketUtils.writeLine(serventSocket, Storage.NEW_ARRIVED + " " + map1.toString().replace(" ", "--"));
//					}
//
//					System.out.println(ServentSingleton.getInstance().getList().toString());
//					System.out.println(GraphSingleton.getInstance());
//					break;
//				case "NEW_ARRIVED":
//					System.out.println("NEW_ARRIVED");
//					System.out.println(nodeAddress);
//
//					String[] parseHashMapLocalArrived = Methods.parseHashMap(nodeAddress);
//					String currentVertex1 = "";
//					HashMap<Integer, String> map2 = new HashMap<Integer,String>(ServentSingleton.getInstance().getList());
//					for (int i = 0; i < parseHashMapLocalArrived.length; i++ ) {
//						String[] parseHashMapLocalAgain = parseHashMapLocalArrived[i].split("=");
//						ServentSingleton.getInstance().getList().put(Integer.parseInt(parseHashMapLocalAgain[0]), parseHashMapLocalAgain[1]);
//
//						String[] parseHashMapLocalAgain1;
//						parseHashMapLocalAgain1 = parseHashMapLocalAgain[1].split(",");
//
//						// Set Triangle Level
//						GraphSingleton.getInstance().addVertex(parseHashMapLocalAgain1[0]);
//
//						// Ako nije trenutni
//						if (!parseHashMapLocalAgain[0].equals("0")) {
//							GraphSingleton.getInstance().addEdge(currentVertex1, parseHashMapLocalAgain1[0]);
//						} else {
//							currentVertex1 = parseHashMapLocalAgain1[0];
//						}
//					}
//
//					System.out.println(ServentSingleton.getInstance().getList().toString());
//					System.out.println(GraphSingleton.getInstance());
//					break;
//				case "PLAY":
//					// Node address is now game info for playing
//
//					parseValue = message[1].split(",");
//
//					System.out.println(parseValue[5]);
//					Object[] object = new Operations().init(
//						parseValue[0],
//						parseValue[1],
//						Integer.parseInt(parseValue[2]),
//						Integer.parseInt(parseValue[3]),
//						Integer.parseInt(parseValue[4]),
//						Integer.parseInt(parseValue[5])
//					);
//					break;
			default:
				System.out.println("Wrong communication.");
				break;
		}
	}
}
