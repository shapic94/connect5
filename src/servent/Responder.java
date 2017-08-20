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
						ServentSingleton.getInstance().updateList("0", ip + ":" + port);

						// CIRCLE CHECK
						try {
							socket = new Socket(ip, Integer.parseInt(port));
						} catch (IOException e) {
							e.printStackTrace();
						}

						SocketUtils.writeLine(
								socket,
								Storage.CIRCLE_CHECK + " test 123123:3123"
						);

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

									// Set Servant emptyLocalChild
									ServentSingleton.getInstance().setEmptyLocalChild(ServentSingleton.getInstance().getEmptyLocalChild() - 1);

									// Set Servant hashMap
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
								} else {

									String[] hasFreeParent = Methods.hasFreeParent(ServentSingleton.getInstance().getList());

									// If has empty global child
									if (hasFreeParent[0] != null) {

										// Set Servant emptyGlobalChild
										ServentSingleton.getInstance().updateList(hasFreeParent[0], hasFreeParent[1] + " " + (Integer.parseInt(hasFreeParent[2]) - 1));


										try {
											socket = new Socket(ip, Integer.parseInt(port));
										} catch (IOException e) {
											e.printStackTrace();
										}

										SocketUtils.writeLine(
											socket,
											Storage.NOTIFY_CHILD + " " +
											Storage.NODE + " " +
											hasFreeParent[1].split(":")[0] + ":" +
											hasFreeParent[1].split(":")[1] + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--")
										);

										String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
										String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());
										if (node1Id != null) {
											String[] node1Address = ServentSingleton.getInstance().getList().get(node1Id).split(":");
											try {
												socket = new Socket(node1Address[0], Integer.parseInt(node1Address[1]));
											} catch (IOException e) {
												e.printStackTrace();
											}

											SocketUtils.writeLine(
													socket,
													Storage.NOTIFY_CHILD + " " +
															Storage.ACCEPT_NODE + " " +
															hasFreeParent[1].split(":")[0] + ":" +
															hasFreeParent[1].split(":")[1] + " " +
															ServentSingleton.getInstance().getList().toString().replace(" ", "--")
											);
										}

										if (node2Id != null) {
											String[] node2Address = ServentSingleton.getInstance().getList().get(node2Id).split(":");
											try {
												socket = new Socket(node2Address[0], Integer.parseInt(node2Address[1]));
											} catch (IOException e) {
												e.printStackTrace();
											}

											SocketUtils.writeLine(
													socket,
													Storage.NOTIFY_CHILD + " " +
															Storage.ACCEPT_NODE + " " +
															hasFreeParent[1].split(":")[0] + ":" +
															hasFreeParent[1].split(":")[1] + " " +
															ServentSingleton.getInstance().getList().toString().replace(" ", "--")
											);
										}

										// Show id
										System.out.println("Id : " + ServentSingleton.getInstance().getId());

										// Show map
										System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

										System.out.println("--------------------------------------------------");
									} else {
										// If not, create new parent

										String parentId = "1." + ServentSingleton.getInstance().getId().replace("1", "0");

										// Set Servant
										ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
										Methods.extendHashMap(ServentSingleton.getInstance().getList());
										ServentSingleton.getInstance().updateList(parentId, ip + ":" + port + " " + Methods.numberOfChildrenGlobal(ServentSingleton.getInstance().getId()));

										// Create socket
										try {
											socket = new Socket(ip, Integer.parseInt(port));
										} catch (IOException e) {
											e.printStackTrace();
										}

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

										// NOTIFY_ALL
										Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
										while (it.hasNext()) {
											Map.Entry pair = (Map.Entry) it.next();

											String[] lastCharacterOfKey = pair.getKey().toString().split("\\.");

											// Notify NODE_1 and NODE_2 with extend id and new map
											if (lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_1) || lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_2)) {
												String[] addressOfNode1 = ServentSingleton.getInstance().getList().get(pair.getKey()).split(":");

												try {
													socket = new Socket(addressOfNode1[0], Integer.parseInt(addressOfNode1[1]));
												} catch (IOException e) {
													e.printStackTrace();
												}

												SocketUtils.writeLine(
														socket,
														Storage.NOTIFY_ALL + " " +
																Storage.ID_MAP + " " +
																socket.getInetAddress().getHostAddress() + ":" +
																ServentListener.LISTENER_PORT + " " +
																ServentSingleton.getInstance().getList().toString().replace(" ", "--")
												);

											} else if (!pair.getKey().toString().equals(parentId) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
												// Notify other parent if exist
												String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
												String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

												try {
													socket = new Socket(addressOfNode1[0], Integer.parseInt(addressOfNode1[1]));
												} catch (IOException e) {
													e.printStackTrace();
												}

												SocketUtils.writeLine(
														socket,
														Storage.NOTIFY_ALL + " " +
																Storage.ID + " " +
																socket.getInetAddress().getHostAddress() + ":" +
																ServentListener.LISTENER_PORT + " " +
																ServentSingleton.getInstance().getId()
												);
											}
										}

										// CIRCLE CHECK
//										try {
//											socket = new Socket(ip, Integer.parseInt(port));
//										} catch (IOException e) {
//											e.printStackTrace();
//										}
//
//										SocketUtils.writeLine(
//												socket,
//												Storage.CIRCLE_CHECK + " test 123123:3123"
//										);

										// Show id
										System.out.println("Id : " + ServentSingleton.getInstance().getId());

										// Show map
										System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

										System.out.println("--------------------------------------------------");
									}
								}

							} else {
								// Go to global parent

								String[] addressParent = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList())).split(":");

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

								String parsedId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1);
								String emptyChildId = Methods.getEmptyChildId(ServentSingleton.getInstance().getList());

								// Set Servant emptyLocalChild
								ServentSingleton.getInstance().setEmptyLocalChild(ServentSingleton.getInstance().getEmptyLocalChild() - 1);

								// Set Servant hashMap
								ServentSingleton.getInstance().updateList(ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + emptyChildId, ip + ":" + port);

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
									parsedId + emptyChildId
								);

								// Notify child
								if (ServentSingleton.getInstance().getList().containsKey(parsedId + Storage.NODE_1) && emptyChildId != Storage.NODE_1) {

									String[] addressChild = ServentSingleton.getInstance().getList().get(parsedId + Storage.NODE_1).split(":");

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
								} else if (ServentSingleton.getInstance().getList().containsKey(parsedId + Storage.NODE_2) && emptyChildId != Storage.NODE_2) {

									String[] addressChild = ServentSingleton.getInstance().getList().get(parsedId + Storage.NODE_2).split(":");

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
								} else

								// Show id
								System.out.println("Id : " + ServentSingleton.getInstance().getId());

								// Show map
								System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

								System.out.println("--------------------------------------------------");
							} else {

								String[] hasFreeParent = Methods.hasFreeParent(ServentSingleton.getInstance().getList());

								// If has empty global child
								if (hasFreeParent[0] != null) {

									// Set Servant emptyGlobalChild
									ServentSingleton.getInstance().updateList(hasFreeParent[0], hasFreeParent[1] + " " + (Integer.parseInt(hasFreeParent[2]) - 1));


									try {
										socket = new Socket(ip, Integer.parseInt(port));
									} catch (IOException e) {
										e.printStackTrace();
									}

									SocketUtils.writeLine(
											socket,
											Storage.NOTIFY_CHILD + " " +
													Storage.NODE + " " +
													hasFreeParent[1].split(":")[0] + ":" +
													hasFreeParent[1].split(":")[1] + " " +
													ServentSingleton.getInstance().getList().toString().replace(" ", "--")
									);


									String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
									String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());
									if (node1Id != null) {
										String[] node1Address = ServentSingleton.getInstance().getList().get(node1Id).split(":");
										try {
											socket = new Socket(node1Address[0], Integer.parseInt(node1Address[1]));
										} catch (IOException e) {
											e.printStackTrace();
										}

										SocketUtils.writeLine(
												socket,
												Storage.NOTIFY_CHILD + " " +
														Storage.ACCEPT_NODE + " " +
														hasFreeParent[1].split(":")[0] + ":" +
														hasFreeParent[1].split(":")[1] + " " +
														ServentSingleton.getInstance().getList().toString().replace(" ", "--")
										);
									}

									if (node2Id != null) {
										String[] node2Address = ServentSingleton.getInstance().getList().get(node2Id).split(":");
										try {
											socket = new Socket(node2Address[0], Integer.parseInt(node2Address[1]));
										} catch (IOException e) {
											e.printStackTrace();
										}

										SocketUtils.writeLine(
												socket,
												Storage.NOTIFY_CHILD + " " +
														Storage.ACCEPT_NODE + " " +
														hasFreeParent[1].split(":")[0] + ":" +
														hasFreeParent[1].split(":")[1] + " " +
														ServentSingleton.getInstance().getList().toString().replace(" ", "--")
										);
									}

									// Show id
									System.out.println("Id : " + ServentSingleton.getInstance().getId());

									// Show map
									System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

									System.out.println("--------------------------------------------------");

								} else {
									// If not, create new parent
									String parentId = "1." + ServentSingleton.getInstance().getId().replace("1", "0");

									// Set Servant
									ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
									Methods.extendHashMap(ServentSingleton.getInstance().getList());
									ServentSingleton.getInstance().updateList(parentId, ip + ":" + port + " " + Methods.numberOfChildrenGlobal(ServentSingleton.getInstance().getId()));

									// Create socket
									try {
										socket = new Socket(ip, Integer.parseInt(port));
									} catch (IOException e) {
										e.printStackTrace();
									}

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

									// NOTIFY_ALL
									Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
									while (it.hasNext()) {
										Map.Entry pair = (Map.Entry) it.next();

										String[] lastCharacterOfKey = pair.getKey().toString().split("\\.");

										// Notify NODE_1 and NODE_2 with extend id and new map
										if (lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_1) || lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_2)) {
											String[] addressOfNode1 = ServentSingleton.getInstance().getList().get(pair.getKey()).split(":");

											try {
												socket = new Socket(addressOfNode1[0], Integer.parseInt(addressOfNode1[1]));
											} catch (IOException e) {
												e.printStackTrace();
											}

											SocketUtils.writeLine(
													socket,
													Storage.NOTIFY_ALL + " " +
															Storage.ID_MAP + " " +
															socket.getInetAddress().getHostAddress() + ":" +
															ServentListener.LISTENER_PORT + " " +
															ServentSingleton.getInstance().getList().toString().replace(" ", "--")
											);

										} else if (!pair.getKey().toString().equals(parentId) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
											// Notify other parent if exist
											String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
											String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

											try {
												socket = new Socket(addressOfNode1[0], Integer.parseInt(addressOfNode1[1]));
											} catch (IOException e) {
												e.printStackTrace();
											}

											SocketUtils.writeLine(
													socket,
													Storage.NOTIFY_ALL + " " +
															Storage.ID + " " +
															socket.getInetAddress().getHostAddress() + ":" +
															ServentListener.LISTENER_PORT + " " +
															ServentSingleton.getInstance().getId()
											);
										}
									}

									// CIRCLE CHECK
//									try {
//										socket = new Socket(ip, Integer.parseInt(port));
//									} catch (IOException e) {
//										e.printStackTrace();
//									}
//
//									SocketUtils.writeLine(
//											socket,
//											Storage.CIRCLE_CHECK + " test 123123:3123"
//									);

									// Show id
									System.out.println("Id : " + ServentSingleton.getInstance().getId());

									// Show map
									System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

									System.out.println("--------------------------------------------------");
								}
							}
						} else {
							// Go to local parent
						}

						break;
					case "ACCEPT_NODE":
						String map = message[3];

						String id = message[4];

						// Set Servant
						ServentSingleton.getInstance().setEmptyLocalChild(2);
						ServentSingleton.getInstance().setId(id);
						ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));
						ServentSingleton.getInstance().updateList(id, ip + ":" + ServentListener.LISTENER_PORT);

						// CIRCLE CHECK
						try {
							socket = new Socket(ip, ServentListener.LISTENER_PORT);
						} catch (IOException e) {
							e.printStackTrace();
						}

						SocketUtils.writeLine(
								socket,
								Storage.CIRCLE_CHECK + " test 123123:3123"
						);

						// Show id
						System.out.println("Id : " + ServentSingleton.getInstance().getId());

						// Show map
						System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

						System.out.println("--------------------------------------------------");
						break;
					case "FREE_FIELD":
						String childParentId = message[3];
						String childParentFreeFields = message[4];

						// if contains number of freeParent
						if (ServentSingleton.getInstance().getList().containsKey(childParentId)) {
							System.out.println(childParentId);
							if (ServentSingleton.getInstance().getList().get(childParentId).contains(" ")) {
								String[] childParentInMap = ServentSingleton.getInstance().getList().get(childParentId).split(" "); // ip:port 4
								String[] parseChildParentInMap = childParentInMap[0].split(":");
								ServentSingleton.getInstance().updateList(childParentId, parseChildParentInMap[0] + ":" + port + " " + (Integer.parseInt(childParentInMap[1]) + Integer.parseInt(childParentFreeFields)));
							} else {
								System.out.println(ServentSingleton.getInstance().getList());
							}

							String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
							String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());

							System.out.println(node1Id);
							System.out.println(node2Id);
							if (node1Id != null) {
								System.out.println("node 1");
								String[] node1Address = ServentSingleton.getInstance().getList().get(node1Id).split(":");

								if (!ServentListener.isPortInUse(Integer.parseInt(node1Address[1]))) {
									try {
										socket = new Socket(node1Address[0], Integer.parseInt(node1Address[1]));
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

							if (node2Id != null) {
								System.out.println("node 2");
								String[] node2Address = ServentSingleton.getInstance().getList().get(node2Id).split(":");

								if (!ServentListener.isPortInUse(Integer.parseInt(node2Address[1]))) {
									try {
										socket = new Socket(node2Address[0], Integer.parseInt(node2Address[1]));
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

							System.out.println(ServentSingleton.getInstance().getList());
							// Notify all other parents
							Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry pair = (Map.Entry) it.next();

								if (Methods.isLocalParent(pair.getKey().toString()) && !Methods.isGlobalParent(pair.getKey().toString()) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
									String[] otherLocalParentAddress;
									if (pair.getValue().toString().contains(" ")) {
										otherLocalParentAddress = pair.getValue().toString().split(" ");
										otherLocalParentAddress = otherLocalParentAddress[0].split(":");
									} else {
										otherLocalParentAddress = pair.getValue().toString().split(":");
									}

									if (!ServentListener.isPortInUse(Integer.parseInt(otherLocalParentAddress[1]))) {
										try {
											socket = new Socket(otherLocalParentAddress[0], Integer.parseInt(otherLocalParentAddress[1]));
										} catch (IOException e) {
											e.printStackTrace();
										}

										SocketUtils.writeLine(
												socket,
												Storage.NOTIFY_PARENT + " " +
														Storage.ACCEPT_NODE + " " +
														socket.getInetAddress().getHostAddress() + ":" +
														ServentListener.LISTENER_PORT + " " +
														ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
														pair.getKey().toString()
										);
									}
								}
							}

							// If it is not global parent
							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								String parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(parentId).split(":");

								System.out.println(addressOfParent[0]);
								System.out.println(addressOfParent[1]);
								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId() + " " +
												childParentFreeFields
								);
							}

							// Show id
							System.out.println("Id : " + ServentSingleton.getInstance().getId());

							// Show map
							System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

							System.out.println("--------------------------------------------------");
						}

						break;
				}
				break;
			case "NOTIFY_CHILD":
				switch (localKey) {
					case "NODE":
						// Create socket
						try {
							socket = new Socket(ip, Integer.parseInt(port));
						} catch (IOException e) {
							e.printStackTrace();
						}

						// Call global parent
						SocketUtils.writeLine(
							socket,
							Storage.NOTIFY_PARENT + " " +
							Storage.NODE + " " +
							socket.getInetAddress().getHostAddress() + ":" +
							ServentListener.LISTENER_PORT
						);
						break;
					case "ACCEPT_NODE":

						String map = message[3];

						// If accept new node
						if (message.length == 5) {
							String id = message[4];

							// Set Servant
							ServentSingleton.getInstance().setId(id);
							ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));

							// CIRCLE CHECK
							try {
								socket = new Socket(ip, ServentListener.LISTENER_PORT);
							} catch (IOException e) {
								e.printStackTrace();
							}

							SocketUtils.writeLine(
									socket,
									Storage.CIRCLE_CHECK + " test 123123:3123"
							);

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
			case "NOTIFY_ALL":
				switch (localKey) {
					case "ID":
						// PARENT NOTIFY

						String parentId = message[3];

						// Set Servant
						// Extend ID
						ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
						// Extend map
						Methods.extendHashMap(ServentSingleton.getInstance().getList());

						// NOTIFY_ALL
						Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pair = (Map.Entry) it.next();

							String[] lastCharacterOfKey = pair.getKey().toString().split("\\.");

							// Notify NODE_1 and NODE_2 with extend id and new map
							if (lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_1) || lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_2)) {
								String[] addressOfNode1 = ServentSingleton.getInstance().getList().get(pair.getKey()).split(":");

								try {
									socket = new Socket(addressOfNode1[0], Integer.parseInt(addressOfNode1[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_ALL + " " +
												Storage.ID_MAP + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--")
								);

							} else if (!pair.getKey().toString().equals(parentId) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
								// Notify other parent if exist
								String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
								String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

								try {
									socket = new Socket(addressOfNode1[0], Integer.parseInt(addressOfNode1[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_ALL + " " +
												Storage.ID + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId()
								);
							}
						}

						// Show id
						System.out.println("Id : " + ServentSingleton.getInstance().getId());

						// Show map
						System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

						System.out.println("--------------------------------------------------");
						break;
					case "ID_MAP":
						// CHILD NOTIFY

						String map = message[3];

						// Set Servant
						// Extend ID
						ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
						// Set new map from parent
						ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));

						// Show id
						System.out.println("Id : " + ServentSingleton.getInstance().getId());

						// Show map
						System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

						System.out.println("--------------------------------------------------");
						break;
				}
				break;

			case "CIRCLE_CHECK":

				// if it is parent
				if (Methods.isLocalParent(ServentSingleton.getInstance().getId())) {
					String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
					String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());

					// if NODE_2 exist and is NOT alive
					if (node2Id != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(node2Id).split(":")[1]))) {

						// if NODE_1 exist and is NOT alive
						if (node1Id != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(node1Id).split(":")[1]))) {

							System.out.println("Node : 1 [DEAD]");
							System.out.println("Node : 2 [DEAD]");

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(node1Id);
							ServentSingleton.getInstance().getList().remove(node2Id);
							ServentSingleton.getInstance().setEmptyLocalChild(2);

							// Notify parent
							// if it is NOT global parent
//							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								String parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId() + " " +
												2
								);
//							}
						} else {
							System.out.println("Node : 2 [DEAD]");

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(node2Id);

							if (node1Id != null) {
								ServentSingleton.getInstance().setEmptyLocalChild(1);
							} else {
								ServentSingleton.getInstance().setEmptyLocalChild(2);
							}

							// Notify parent
							// if it is NOT global parent
//							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {
								String parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId() + " " +
												1
								);

//							}
						}
					} else if(node1Id != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(node1Id).split(":")[1]))) {
						// if NODE_1 exist and is NOT alive

						System.out.println("Node : 1 [DEAD]");

						// Set Servant
						// Update map
						ServentSingleton.getInstance().getList().remove(node1Id);

						if (node2Id != null) {
							ServentSingleton.getInstance().setEmptyLocalChild(1);
						} else {
							ServentSingleton.getInstance().setEmptyLocalChild(2);
						}

						// Notify parent
						// if it is NOT global parent
//						if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

							String parentId = Methods.getParent(ServentSingleton.getInstance().getList());
							String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

							try {
								socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
							} catch (IOException e) {
								e.printStackTrace();
							}

							SocketUtils.writeLine(
									socket,
									Storage.NOTIFY_PARENT + " " +
											Storage.FREE_FIELD + " " +
											socket.getInetAddress().getHostAddress() + ":" +
											ServentListener.LISTENER_PORT + " " +
											ServentSingleton.getInstance().getId() + " " +
											1
							);
//						}
					}
				} else if (Methods.isNode1(ServentSingleton.getInstance().getId())) {
					// If it is NODE_1

					String parentId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + "0";
					String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());

					// if Parent exist and is NOT alive
					if (parentId != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(parentId).split(":")[1]))) {

						// if NODE_2 exist and is NOT alive
						if (node2Id != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(node2Id).split(":")[1]))) {

							System.out.println("Parent : [DEAD]");
							System.out.println("Node : 2 [DEAD]");

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(parentId);
							ServentSingleton.getInstance().getList().remove(node2Id);

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);
							ServentSingleton.getInstance().setEmptyLocalChild(2);

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);


							// Notify parent
							// if it is NOT global parent
//							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId() + " " +
												2
								);
//							}
						} else {

							System.out.println("Parent : [DEAD]");
							System.out.println("Mapa : " + ServentSingleton.getInstance().getList());
							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(parentId);

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);
							if (node2Id != null) {
								ServentSingleton.getInstance().setEmptyLocalChild(1);
							} else {
								ServentSingleton.getInstance().setEmptyLocalChild(2);
							}

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);

							// Notify parent
							// if it is NOT global parent
//							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId() + " " +
												1
								);
							System.out.println("Mapa : " + ServentSingleton.getInstance().getList());
//							}
						}
					} else if(node2Id != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(node2Id).split(":")[1]))) {
						// if NODE_2 exist and is NOT alive

						System.out.println("Node : 2 [DEAD]");

						// Set Servant
						// Update map
						ServentSingleton.getInstance().getList().remove(node2Id);

						// Notify parent
						// if it is NOT global parent
//						if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

							parentId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + "0";
							String[] addressOfParent = ServentSingleton.getInstance().getList().get(parentId).split(":");

							try {
								socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
							} catch (IOException e) {
								e.printStackTrace();
							}

							SocketUtils.writeLine(
									socket,
									Storage.NOTIFY_PARENT + " " +
											Storage.FREE_FIELD + " " +
											socket.getInetAddress().getHostAddress() + ":" +
											ServentListener.LISTENER_PORT + " " +
											node2Id + " " +
											1

							);
//						}

						// Show id
						System.out.println("Id : " + ServentSingleton.getInstance().getId());

						// Show map
						System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

						System.out.println("--------------------------------------------------");
					}
				} else if (Methods.isNode2(ServentSingleton.getInstance().getId())) {
					// If it is NODE_2

					String parentId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + "0";
					String node1Id = Methods.getNode2(ServentSingleton.getInstance().getList());

					// if Parent exist and is NOT alive
					if (node1Id != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(node1Id).split(":")[1]))) {

						// if NODE_2 exist and is NOT alive
						if (parentId != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(parentId).split(":")[1]))) {

							System.out.println("Parent : [DEAD]");
							System.out.println("Node : 1 [DEAD]");

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(parentId);
							ServentSingleton.getInstance().getList().remove(node1Id);
							ServentSingleton.getInstance().setEmptyLocalChild(2);

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);


							// Notify parent
							// if it is NOT global parent
//							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId() + " " +
												2
								);
//							}

						} else {

							System.out.println("Node : 1 [DEAD]");

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(node1Id);

							// Notify parent
							// if it is NOT global parent
//							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								parentId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + "0";
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(parentId).split(":");

								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												node1Id + " " +
												1
								);
//							}

							// Show id
							System.out.println("Id : " + ServentSingleton.getInstance().getId());

							// Show map
							System.out.println("Mapa : " + ServentSingleton.getInstance().getList());

							System.out.println("--------------------------------------------------");
						}
					} else if(parentId != null && ServentListener.isPortInUse(Integer.parseInt(ServentSingleton.getInstance().getList().get(parentId).split(":")[1]))) {

						// if NODE_2 exist and is NOT alive

						if (node1Id == null) {

							System.out.println("Parent : [DEAD]");

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(parentId);

							if (node1Id != null) {
								ServentSingleton.getInstance().setEmptyLocalChild(1);
							} else {
								ServentSingleton.getInstance().setEmptyLocalChild(2);
							}

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);

							// Notify parent
							// if it is NOT global parent
//							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

								try {
									socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
								} catch (IOException e) {
									e.printStackTrace();
								}

								SocketUtils.writeLine(
										socket,
										Storage.NOTIFY_PARENT + " " +
												Storage.FREE_FIELD + " " +
												socket.getInetAddress().getHostAddress() + ":" +
												ServentListener.LISTENER_PORT + " " +
												ServentSingleton.getInstance().getId() + " " +
												1
								);
//							}
						}
					}
				}

				try {
					Thread.sleep(2000);
				} catch	(InterruptedException e) {
					e.printStackTrace();
				}

				// call parent again
				String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

				try {
					socket = new Socket(addressOfParent[0], Integer.parseInt(addressOfParent[1]));
				} catch (IOException e) {
					e.printStackTrace();
				}

				SocketUtils.writeLine(
						socket,
						Storage.CIRCLE_CHECK + " test 123123:3123"
				);

				break;
			default:
				System.out.println("Wrong communication.");
				break;
		}
	}
}
