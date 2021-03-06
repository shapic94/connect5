package servent;
import connect5.Operations;
import global.Methods;
import global.Storage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
	String info = null;

	String[] isAvailableAddress;
	String[] isAvailableAddressParent;
	String[] isAvailableAddressNode1;
	String[] isAvailableAddressNode2;


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

		// If line null, break
		if (line == null) {
			return;
		}

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
						try {
							ServentSingleton.getInstance().updateList("0", Methods.getAddress());
							ServentSingleton.getInstance().updateProccess("0", "0");
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}

						ServentSingleton.getInstance().setResultPlayer1(0);
						ServentSingleton.getInstance().setResultPlayer2(0);
						ServentSingleton.getInstance().setLocalResultPlayer1(0);
						ServentSingleton.getInstance().setLocalResultPlayer2(0);
						ServentSingleton.getInstance().setIzigravanje("0");

						// CIRCLE CHECK
						// Create socket
						info = Storage.CIRCLE_CHECK + " test 123123:3123";
						try {
							ServentListener.createSocket(Methods.getIp(), Integer.toString(Methods.getPort()), info);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}


						// Print Info
						try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						break;
					case "NODE":

						// If it is created node
						if (ServentSingleton.getInstance().getId() != null) {

							// If it is global parent
							if (Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								// If has empty local child
								if (ServentSingleton.getInstance().getEmptyLocalChild() > 0) {

									String parsedId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1);
									String emptyChildId = Methods.getEmptyChildId(ServentSingleton.getInstance().getList());

									// Set Servant emptyLocalChild
									ServentSingleton.getInstance().setEmptyLocalChild(ServentSingleton.getInstance().getEmptyLocalChild() - 1);

									// Set Servant hashMap
									ServentSingleton.getInstance().updateList(parsedId + emptyChildId, ip + ":" + port);
									ServentSingleton.getInstance().updateProccess(parsedId + emptyChildId, "0");

									// Create socket
									try {
										info = Storage.NOTIFY_CHILD + " " +
                                                Storage.ACCEPT_NODE + " " +
                                                Methods.getAddress() + " " +
                                                ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
                                                ServentSingleton.getInstance().getProccess().toString().replace(" ", "--") + " " +
                                                parsedId + emptyChildId;

										ServentListener.createSocket(ip, port, info);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									}

									// Notify child
									if (ServentSingleton.getInstance().getList().containsKey(parsedId + Storage.NODE_1) && emptyChildId != Storage.NODE_1) {

										String[] addressChild = ServentSingleton.getInstance().getList().get(parsedId + Storage.NODE_1).split(":");

										// Create socket
										try {
											info = Storage.NOTIFY_CHILD + " " +
													Storage.ACCEPT_NODE + " " +
													Methods.getAddress() + " " +
													ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
													ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

											ServentListener.createSocket(addressChild[0], addressChild[1], info);
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									} else if (ServentSingleton.getInstance().getList().containsKey(parsedId + Storage.NODE_2) && emptyChildId != Storage.NODE_2) {

										String[] addressChild = ServentSingleton.getInstance().getList().get(parsedId + Storage.NODE_2).split(":");

										// Create socket
										try {
											info = Storage.NOTIFY_CHILD + " " +
													Storage.ACCEPT_NODE + " " +
													Methods.getAddress() + " " +
													ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
													ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

											ServentListener.createSocket(addressChild[0], addressChild[1], info);
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									}

									// Print Info
									try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
								} else {

									String[] hasFreeParent = Methods.hasFreeParent(ServentSingleton.getInstance().getList());

									// If has empty global child
									if (hasFreeParent[0] != null) {

										// Set Servant emptyGlobalChild
										ServentSingleton.getInstance().updateList(hasFreeParent[0], hasFreeParent[1] + " " + (Integer.parseInt(hasFreeParent[2]) - 1));

										// Create socket
										info = Storage.NOTIFY_CHILD + " " +
												Storage.NODE + " " +
												hasFreeParent[1].split(":")[0] + ":" + hasFreeParent[1].split(":")[1] + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--");

										ServentListener.createSocket(ip, port, info);


										String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
										String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());
										if (node1Id != null) {
											String[] node1Address = ServentSingleton.getInstance().getList().get(node1Id).split(":");

											// Create socket
											info = Storage.NOTIFY_CHILD + " " +
													Storage.ACCEPT_NODE + " " +
													hasFreeParent[1].split(":")[0] + ":" + hasFreeParent[1].split(":")[1] + " " +
													ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
													ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

											ServentListener.createSocket(node1Address[0], node1Address[1], info);

										}

										if (node2Id != null) {
											String[] node2Address = ServentSingleton.getInstance().getList().get(node2Id).split(":");

											// Create socket
											info = Storage.NOTIFY_CHILD + " " +
													Storage.ACCEPT_NODE + " " +
													hasFreeParent[1].split(":")[0] + ":" + hasFreeParent[1].split(":")[1] + " " +
													ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
													ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

											ServentListener.createSocket(node2Address[0], node2Address[1], info);
										}

										// Print Info
										try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
									} else {
										// If not, create new parent

										String parentId = "1." + ServentSingleton.getInstance().getId().replace("1", "0");

										// Set Servant
										ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
										Methods.extendHashMap(ServentSingleton.getInstance().getList());
										Methods.extendHashMap(ServentSingleton.getInstance().getProccess());
										ServentSingleton.getInstance().updateList(parentId, ip + ":" + port + " " + Methods.numberOfChildrenGlobal(ServentSingleton.getInstance().getId()));
										ServentSingleton.getInstance().updateProccess(parentId, "0." +  (Methods.numberOfChildrenGlobal(parentId) - Integer.parseInt(ServentSingleton.getInstance().getList().get(parentId).split(" ")[1])));


										// Prepare new map
										HashMap<String, String> parentHashmapList = new HashMap<String, String>();
										parentHashmapList.put(ServentSingleton.getInstance().getId(), ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()));

										HashMap<String, String> parentHashmapProccess = new HashMap<String, String>();
										parentHashmapProccess.put(ServentSingleton.getInstance().getId(), ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()));

										// Create socket
										try {
											info = Storage.NOTIFY_PARENT + " " +
													Storage.ACCEPT_NODE + " " +
													Methods.getAddress() + " " +
													parentHashmapList.toString().replace(" ", "--") + " " +
													parentHashmapProccess.toString().replace(" ", "--") + " " +
													parentId;

											ServentListener.createSocket(ip, port, info);
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}

										// NOTIFY_ALL
										Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
										while (it.hasNext()) {
											Map.Entry pair = (Map.Entry) it.next();

											String[] lastCharacterOfKey = pair.getKey().toString().split("\\.");

											// Notify NODE_1 and NODE_2 with extend id and new map
											if (lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_1) || lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_2)) {
												String[] addressOfNode1 = ServentSingleton.getInstance().getList().get(pair.getKey()).split(":");

												// Create socket
												try {
													info = Storage.NOTIFY_ALL + " " +
															Storage.ID_MAP + " " +
															Methods.getAddress() + " " +
															ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
															ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

													ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
												} catch (UnknownHostException e) {
													e.printStackTrace();
												}
											} else if (!pair.getKey().toString().equals(parentId) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
												// Notify other parent if exist
												String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
												String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

												// Create socket
												try {
													info = Storage.NOTIFY_ALL + " " +
															Storage.ID + " " +
															Methods.getAddress() + " " +
															ServentSingleton.getInstance().getId();

													ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
												} catch (UnknownHostException e) {
													e.printStackTrace();
												}
											}
										}

										// Print Info
										try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
									}
								}

							} else {
								// Go to global parent

								String[] addressParent = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList())).split(":");

								// Create socket
								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.NODE + " " +
										addressParent[0] + ":" + addressParent[1];

								ServentListener.createSocket(ip, port, info);
							}
						} else {

							// Create socket
							try {
								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.NODE + " " +
										Methods.getAddress();

								ServentListener.createSocket(ip, port, info);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}
						}
						break;
					case "GAME":
						// If it is created node
						if (ServentSingleton.getInstance().getId() != null) {

							// If it is global parent
							if (Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								String[] addressParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_ALL + " " +
											Storage.GAME + " " +
											Methods.getAddress() + " " +
											message[3] + " " +
											ServentSingleton.getInstance().getId() + " " +
											message[4];

									ServentListener.createSocket(addressParent[0], addressParent[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							} else {
								// Go to global parent

								String[] addressParent = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList())).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_GLOBAL_PARENT + " " +
											Storage.GAME + " " +
											Methods.getAddress() + " " +
											message[3] + " " +
											message[4];

									ServentListener.createSocket(addressParent[0], addressParent[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}
						}
						break;
					case "GAME_FINISHED":

						// If it is created node
						if (ServentSingleton.getInstance().getId() != null) {

							// If it is global parent
							if (Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								// Reduce izigravanje
								if (message[4].equals("1") || (message[4].equals("0") && !Methods.isGlobalParent(message[3]))) {

									String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
									String[] testProcesa1;
									if (testProcesa.toString().contains(".")) {
										testProcesa1 = testProcesa.toString().split("\\.");
										if (message[5].equals("1")) {
											System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) - 1) + "." + testProcesa1[1]) + "] - " + ServentSingleton.getInstance().getProccess());
											ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) - 1) + "." + testProcesa1[1]);
										} else {
											System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) -1 )) + "] " + ServentSingleton.getInstance().getProccess());
											ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) -1 ));
										}
									} else {
										System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) - 1) + "] " + ServentSingleton.getInstance().getProccess());
										ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) - 1));
									}
								} else {
									System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3]))) + "] " + ServentSingleton.getInstance().getProccess());
									ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3]))));
								}

								Iterator iterateMoreGame;

								// proveri da li neko izigrava
								// NOTIFY_ALL
								iterateMoreGame = ServentSingleton.getInstance().getProccess().entrySet().iterator();
								while (iterateMoreGame.hasNext()) {
									Map.Entry pair = (Map.Entry) iterateMoreGame.next();

									// Ako nije parent
									if (!pair.getValue().toString().contains(".")) {

										// Ako ima slobodnih
										if (Integer.parseInt(pair.getValue().toString()) > 0) {

											// Ako childovi imaju izigravanja, salji njima note
											if (Methods.isNode1(pair.getKey().toString()) || Methods.isNode2(pair.getKey().toString())) {
//												 Ako njegovi childovi imaju izigrivanja, posalji ih kod njih, da im uzme izigravanja momentalno

												String[] notifyAddress;

												if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
													String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
													notifyAddress = parseToNotifyAddress[0].split(":");
												} else {
													notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
												}

												String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
												String[] testProcesa1;
												if (testProcesa.toString().contains(".")) {
													testProcesa1 = testProcesa.toString().split("\\.");
													if (Methods.isLocalParent(message[3])) {
															System.out.println("Pitaj za izigravanja1.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
													} else {
															System.out.println("Pitaj za izigravanja1.2 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
														ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
													}
												} else {
														System.out.println("Pitaj za izigravanja1.3 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
												}

												ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());


												// Create socket
												info = Storage.NOTIFY_CHILD + " " +
														Storage.MORE_GAME + " " +
														ip + ":" + port + " " +
														message[3] + " " +
														ServentSingleton.getInstance().getId();

												ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);

												break;
											} else {
												// Ako parent ima izigravanja
												int myTimes = ServentSingleton.getInstance().getTempTimes();
												int hisTimes = myTimes / 2;

												if (Storage.TIMES_MIN_SEND < hisTimes) {
													System.out.println(ServentSingleton.getInstance().getTempTimes() + " - - - " + myTimes);
													ServentSingleton.getInstance().setTempTimes(myTimes - hisTimes);

													try {
														System.out.println("[" + ip + ":" + port + "] => [" + Methods.getAddress() + "] Imao sam " + myTimes + " izigravanja, dao sam " + (myTimes / 2) + " i ostalo mi je " + (myTimes - hisTimes));
													} catch (UnknownHostException e) {
														e.printStackTrace();
													}

													String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
													String[] testProcesa1;
													if (testProcesa.toString().contains(".")) {
														testProcesa1 = testProcesa.toString().split("\\.");
														if (Methods.isLocalParent(message[3])) {
															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
															ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
														} else {
															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1)) + "] " + ServentSingleton.getInstance().getProccess());
															ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1));
														}
													} else {
														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
													}

													// Create socket
													try {
														info = Storage.NOTIFY_CHILD + " " +
																Storage.ACCEPT_MORE_GAME + " " +
																Methods.getAddress() + " " +
																hisTimes;

														ServentListener.createSocket(ip, port, info);
													} catch (UnknownHostException e) {
														e.printStackTrace();
													}
												}
												break;
											}
										}
									} else {
										// Ako je on sam slobodan
										if (Integer.parseInt(pair.getValue().toString().split("\\.")[0]) > 0 || Integer.parseInt(pair.getValue().toString().split("\\.")[1]) > 0) {

											// Ako njegovi childovi imaju izigrivanja, posalji ih kod njih, da im uzme izigravanja momentalno

											String[] notifyAddress;

											if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
												String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
												notifyAddress = parseToNotifyAddress[0].split(":");
											} else {
												notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
											}

											String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
											String[] testProcesa1;
											if (testProcesa.toString().contains(".")) {
												testProcesa1 = testProcesa.toString().split("\\.");
												if (Methods.isLocalParent(message[3])) {
														System.out.println("Pitaj za izigravanja1.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
												} else {
														System.out.println("Pitaj za izigravanja1.2 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
													ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
												}
											} else {
													System.out.println("Pitaj za izigravanja1.3 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
												ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
											}

											ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());

											// Create socket
											info = Storage.NOTIFY_CHILD + " " +
													Storage.MORE_GAME + " " +
													ip + ":" + port + " " +
													message[3] + " " + ServentSingleton.getInstance().getId();


											ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
											break;
										}
									}
								}

							} else {

								// Reduce izigravanje
								if (message[4].equals("1") || (message[4].equals("0") && !Methods.isGlobalParent(ServentSingleton.getInstance().getId()))) {

									String testProcesa = ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId());
									String[] testProcesa1;
									if (testProcesa.toString().contains(".")) {
										testProcesa1 = testProcesa.toString().split("\\.");
//										if (Methods.isLocalParent(ServentSingleton.getInstance().getId())) {
//											System.out.println("Javio see breee " + ip + ":" + port + " - " + ServentSingleton.getInstance().getId() + " - - [" + ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) - 1) + "." + testProcesa1[1]) + "] - " + ServentSingleton.getInstance().getProccess());
//											ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), Integer.toString(Integer.parseInt(testProcesa1[0]) - 1) + "." + testProcesa1[1]);
//										} else {
											System.out.println("Javio see breee " + ip + ":" + port + " - " + ServentSingleton.getInstance().getId() + " - - [" + ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) -1 )) + "] " + ServentSingleton.getInstance().getProccess());
											ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) -1 ));
//										}
									} else {
										System.out.println("Javio see breee " + ip + ":" + port + " - " + ServentSingleton.getInstance().getId() + " - - [" + ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId())) - 1) + "] " + ServentSingleton.getInstance().getProccess());
										ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId())) - 1));
									}
								} else {
									System.out.println("Javio see breee " + ip + ":" + port + " - " + ServentSingleton.getInstance().getId() + " - - [" + ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()))) + "] " + ServentSingleton.getInstance().getProccess());
									ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()))));
								}

								// Go to global parent
								String[] addressParent = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList())).split(":");

								// Create socket
								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.GAME_FINISHED + " " +
										ip + ":" + port + " " +
										ServentSingleton.getInstance().getId() + " 1 " + message[5];

								ServentListener.createSocket(addressParent[0], addressParent[1], info);

								break;
							}

						}

//								boolean pass = false;
//
//								if (message[4].equals("1") || (message[4].equals("0") && !Methods.isGlobalParent(message[3]))) {
//
//									String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//									String[] testProcesa1;
//									if (testProcesa.toString().contains(".")) {
//										testProcesa1 = testProcesa.toString().split("\\.");
//										if (Methods.isLocalParent(message[3])) {
//											System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) - 1) + "." + testProcesa1[1]) + "] - " + ServentSingleton.getInstance().getProccess());
//											ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) - 1) + "." + testProcesa1[1]);
//										} else {
//											System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) -1 )) + "] " + ServentSingleton.getInstance().getProccess());
//											ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) -1 ));
//										}
//									} else {
//										System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) - 1) + "] " + ServentSingleton.getInstance().getProccess());
//										ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) - 1));
//									}
//								} else {
//									System.out.println("Javio see breee " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3]))) + "] " + ServentSingleton.getInstance().getProccess());
//									ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3]))));
//								}
//
//								ServentSingleton.getInstance().getWaiting().remove(message[3]);
//
//
//
//								Iterator iterateMoreGame;
//
//								// proveri da li neko izigrava
//								// NOTIFY_ALL
//								iterateMoreGame = ServentSingleton.getInstance().getProccess().entrySet().iterator();
//								while (iterateMoreGame.hasNext()) {
//									Map.Entry pair = (Map.Entry) iterateMoreGame.next();
//
//									// Ako izigrava
//									String proccesses;
//									String proccesses1;
//									if (ServentSingleton.getInstance().getWaiting().containsKey(pair.getKey().toString())) {
//										if (ServentSingleton.getInstance().getWaiting().get(pair.getKey().toString()) != null && ServentSingleton.getInstance().getWaiting().get(pair.getKey().toString()).toString().equals(message[3])) {
//											continue;
//										}
//									}
//									if (!pair.getValue().toString().contains(".")) {
//
//										if (Integer.parseInt(pair.getValue().toString()) > 0) {
//
//											// ne proveravaj onog ko ti je poslao, i ne samog sebe!
//											if (!message[3].equals(pair.getKey().toString()) && !ServentSingleton.getInstance().getId().equals(pair.getKey().toString())) {
//
//												// Ako je mene pitao, a izigravam, dajem mu pola izigravanja da nastavi
//												if (ServentSingleton.getInstance().getId().equals(pair.getKey().toString()) && ServentSingleton.getInstance().isPlaying()) {
//													int myTimes = ServentSingleton.getInstance().getTempTimes();
//													int hisTimes = myTimes / 2;
//
//													if (Storage.TIMES_MIN_SEND < hisTimes) {
//														System.out.println(ServentSingleton.getInstance().getTempTimes() + " - - - " + myTimes);
//														ServentSingleton.getInstance().setTempTimes(myTimes - hisTimes);
//
//														try {
//															System.out.println("[" + ip + ":" + port + "] => [" + Methods.getAddress() + "] Imao sam " + myTimes + " izigravanja, dao sam " + (myTimes / 2) + " i ostalo mi je " + (myTimes - hisTimes));
//														} catch (UnknownHostException e) {
//															e.printStackTrace();
//														}
//
//
////														System.out.println("Posalji izigravanja " + ip + ":" + port + " - - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
////														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//
//														String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//														String[] testProcesa1;
//														if (testProcesa.toString().contains(".")) {
//															testProcesa1 = testProcesa.toString().split("\\.");
//															if (Methods.isLocalParent(message[3])) {
//																System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
//																ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//															} else {
//																System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
//																ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
//															}
//														} else {
//															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//														}
//
//														pass = true;
//
//														// Create socket
//														try {
//															info = Storage.NOTIFY_CHILD + " " +
//																	Storage.ACCEPT_MORE_GAME + " " +
//																	Methods.getAddress() + " " +
//																	hisTimes;
//
//															ServentListener.createSocket(ip, port, info);
//														} catch (UnknownHostException e) {
//															e.printStackTrace();
//														}
//													} else {
//														String[] notifyAddress;
//														String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList()));
//														if (notifyAddressTemp.contains(" ")) {
//															notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
//														} else {
//															notifyAddress = notifyAddressTemp.split(":");
//														}
//
////														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//														String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//														String[] testProcesa1;
//														if (testProcesa.toString().contains(".")) {
//															testProcesa1 = testProcesa.toString().split("\\.");
//															if (Methods.isLocalParent(message[3])) {
////																System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "]");
//																ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//															} else {
////																System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "]");
//																ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
//															}
//														} else {
////															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
//															ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//														}
//
//														pass = true;
//
//														// Create socket
//														info = Storage.NOTIFY_GLOBAL_PARENT + " " +
//																Storage.GAME_FINISHED + " " +
//																ip + ":" + port + " " +
//																message[3] + " 1";
//
//														ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//													}
//													break;
//												} else if (Methods.isNode1(pair.getKey().toString()) || Methods.isNode2(pair.getKey().toString())) {
//													// Ako njegovi childovi imaju izigrivanja, posalji ih kod njih, da im uzme izigravanja momentalno
//
//													String[] notifyAddress;
//
//													if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//														String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//														notifyAddress = parseToNotifyAddress[0].split(":");
//													} else {
//														notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//													}
//
//
////													System.out.println("Pitaj za izigravanja " + notifyAddress[0] + ":" + notifyAddress[1] + " - - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
////													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//
//													String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//													String[] testProcesa1;
//													if (testProcesa.toString().contains(".")) {
//														testProcesa1 = testProcesa.toString().split("\\.");
//														if (Methods.isLocalParent(message[3])) {
//																System.out.println("Pitaj za izigravanja1.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//														} else {
//																System.out.println("Pitaj za izigravanja1.2 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
//														}
//													} else {
//															System.out.println("Pitaj za izigravanja1.3 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//													}
//
//													ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());
//
//													pass = true;
//
//													// Create socket
//													info = Storage.NOTIFY_CHILD + " " +
//															Storage.MORE_GAME + " " +
//															ip + ":" + port + " " +
//															message[3];
//
//													ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//													break;
//												} else if (!Methods.isGlobalParent(pair.getKey().toString())) {
//													// ako je neki drugi parent a ima izigravanja neko njegov, salji ga kod njega !
//													String[] notifyAddress;
//
//													if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//														String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//														notifyAddress = parseToNotifyAddress[0].split(":");
//													} else {
//														notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//													}
//
//
////													System.out.println("Pitaj za izigravanja " + notifyAddress[0] + ":" + notifyAddress[1] + " - - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
////													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//
//													String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//													String[] testProcesa1;
//													if (testProcesa.toString().contains(".")) {
//														testProcesa1 = testProcesa.toString().split("\\.");
//														if (Methods.isLocalParent(message[3])) {
//															System.out.println("Pitaj za izigravanja2.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//														} else {
//															System.out.println("Pitaj za izigravanja2.2 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
//														}
//													} else {
//														System.out.println("Pitaj za izigravanja2.3 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//													}
//
//													ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());
//													pass = true;
//
//													// Create socket
//													info = Storage.NOTIFY_CHILD + " " +
//															Storage.MORE_GAME + " " +
//															ip + ":" + port + " " +
//															message[3] + " " +
//															ServentSingleton.getInstance().getId();
//
//													ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//													break;
//												}
//											}
//										}
//
//									} else {
//										// ne proveravaj onog ko ti je poslao
//										if (!message[3].equals(pair.getKey().toString()) && !ServentSingleton.getInstance().getId().equals(pair.getKey().toString())) {
//											if (Integer.parseInt(pair.getValue().toString().split("\\.")[0]) > 0) {
//												// ako je neki drugi parent a ima izigravanja neko njegov, salji ga kod njega !
//												String[] notifyAddress;
//
//												if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//													String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//													notifyAddress = parseToNotifyAddress[0].split(":");
//												} else {
//													notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//												}
//
//
////												System.out.println("Pitaj za izigravanja3.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
////												ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//
//
//												String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//												String[] testProcesa1;
//												if (testProcesa.toString().contains(".")) {
//													testProcesa1 = testProcesa.toString().split("\\.");
//													if (Methods.isLocalParent(message[3])) {
//														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//													} else {
//														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
//													}
//												} else {
//													System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
//													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//												}
//
//												ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());
//												pass = true;
//
//												// Create socket
//												info = Storage.NOTIFY_CHILD + " " +
//														Storage.MORE_GAME + " " +
//														ip + ":" + port + " " +
//														message[3] + " " +
//														ServentSingleton.getInstance().getId();
//
//												ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//												break;
//											} else if (Integer.parseInt(pair.getValue().toString().split("\\.")[1]) > 0) {
//												// ako je neki drugi parent a ima izigravanja neko njegov, salji ga kod njega !
//												String[] notifyAddress;
//
//												if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//													String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//													notifyAddress = parseToNotifyAddress[0].split(":");
//												} else {
//													notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//												}
//
//
////												System.out.println("Pitaj za izigravanja4.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
////												ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//
//												String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//												String[] testProcesa1;
//												if (testProcesa.toString().contains(".")) {
//													testProcesa1 = testProcesa.toString().split("\\.");
//													if (Methods.isLocalParent(message[3])) {
//														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//													} else {
//														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
//													}
//												} else {
//													System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
//													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//												}
//
//												ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());
//
//												pass = true;
//
//												// Create socket
//												info = Storage.NOTIFY_CHILD + " " +
//														Storage.MORE_GAME + " " +
//														ip + ":" + port + " " +
//														message[3] + " " +
//														ServentSingleton.getInstance().getId();
//
//												ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//												break;
//											}
//										}
//									}
//								}
//
//								if (!pass) {
//									System.out.println("NEMA IZIGRAVANJA - " + ip + ":" + port);
//									try {
//										Methods.printInfo();
//									} catch (UnknownHostException e) {
//										e.printStackTrace();
//									}
//								}
//							} else {
//								// Go to global parent
//
//								String[] addressParent = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList())).split(":");
//
//								// Create socket
//								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
//										Storage.GAME_FINISHED + " " +
//										ip + ":" + port + " " +
//										message[3] + " 1";
//
//								ServentListener.createSocket(ip, port, info);
//							}
//						}
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
								ServentSingleton.getInstance().updateList(parsedId + emptyChildId, ip + ":" + port);
								ServentSingleton.getInstance().updateProccess(parsedId + emptyChildId, "0");

								// Create socket
								try {
									info = Storage.NOTIFY_CHILD + " " +
											Storage.ACCEPT_NODE + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
											ServentSingleton.getInstance().getProccess().toString().replace(" ", "--") + " " +
											parsedId + emptyChildId;

									ServentListener.createSocket(ip, port, info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}

								// Notify child
								if (ServentSingleton.getInstance().getList().containsKey(parsedId + Storage.NODE_1) && emptyChildId != Storage.NODE_1) {

									String[] addressChild = ServentSingleton.getInstance().getList().get(parsedId + Storage.NODE_1).split(":");

									// Create socket
									try {
										info = Storage.NOTIFY_CHILD + " " +
												Storage.ACCEPT_NODE + " " +
												Methods.getAddress() + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
												ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

										ServentListener.createSocket(addressChild[0], addressChild[1], info);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									}
								} else if (ServentSingleton.getInstance().getList().containsKey(parsedId + Storage.NODE_2) && emptyChildId != Storage.NODE_2) {

									String[] addressChild = ServentSingleton.getInstance().getList().get(parsedId + Storage.NODE_2).split(":");

									// Create socket
									try {
										info = Storage.NOTIFY_CHILD + " " +
												Storage.ACCEPT_NODE + " " +
												Methods.getAddress() + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
												ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

										ServentListener.createSocket(addressChild[0], addressChild[1], info);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									}
								}

								// Print Info
								try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
							} else {

								String[] hasFreeParent = Methods.hasFreeParent(ServentSingleton.getInstance().getList());

								// If has empty global child
								if (hasFreeParent[0] != null) {

									// Set Servant emptyGlobalChild
									ServentSingleton.getInstance().updateList(hasFreeParent[0], hasFreeParent[1] + " " + (Integer.parseInt(hasFreeParent[2]) - 1));

									// Create socket
									info = Storage.NOTIFY_CHILD + " " +
											Storage.NODE + " " +
											hasFreeParent[1].split(":")[0] + ":" + hasFreeParent[1].split(":")[1] + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--");

									ServentListener.createSocket(ip, port, info);


									String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
									String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());
									if (node1Id != null) {
										String[] node1Address = ServentSingleton.getInstance().getList().get(node1Id).split(":");

										// Create socket
										info = Storage.NOTIFY_CHILD + " " +
												Storage.ACCEPT_NODE + " " +
												hasFreeParent[1].split(":")[0] + ":" + hasFreeParent[1].split(":")[1] + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
												ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

										ServentListener.createSocket(node1Address[0], node1Address[1], info);
									}

									if (node2Id != null) {
										String[] node2Address = ServentSingleton.getInstance().getList().get(node2Id).split(":");

										// Create socket
										info = Storage.NOTIFY_CHILD + " " +
												Storage.ACCEPT_NODE + " " +
												hasFreeParent[1].split(":")[0] + ":" + hasFreeParent[1].split(":")[1] + " " +
												ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
												ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

										ServentListener.createSocket(node2Address[0], node2Address[1], info);
									}

									// Print Info
									try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
								} else {
									// If not, create new parent
									String parentId = "1." + ServentSingleton.getInstance().getId().replace("1", "0");

									// Set Servant
									ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
									Methods.extendHashMap(ServentSingleton.getInstance().getList());
									Methods.extendHashMap(ServentSingleton.getInstance().getProccess());
									ServentSingleton.getInstance().updateList(parentId, ip + ":" + port + " " + Methods.numberOfChildrenGlobal(ServentSingleton.getInstance().getId()));
//									ServentSingleton.getInstance().updateProccess(parentId, "0");
									ServentSingleton.getInstance().updateProccess(parentId, "0." +  (Methods.numberOfChildrenGlobal(parentId) - Integer.parseInt(ServentSingleton.getInstance().getList().get(parentId).split(" ")[1])));

									// Prepare new map
									HashMap<String, String> parentHashmapList = new HashMap<String, String>();
									parentHashmapList.put(ServentSingleton.getInstance().getId(), ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()));

									HashMap<String, String> parentHashmapProccess = new HashMap<String, String>();
									parentHashmapProccess.put(ServentSingleton.getInstance().getId(), ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId()));

									// Create socket
									try {
										info = Storage.NOTIFY_PARENT + " " +
												Storage.ACCEPT_NODE + " " +
												Methods.getAddress() + " " +
												parentHashmapList.toString().replace(" ", "--") + " " +
												parentHashmapProccess.toString().replace(" ", "--") + " " +
												parentId;

										ServentListener.createSocket(ip, port, info);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									}

									// NOTIFY_ALL
									Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
									while (it.hasNext()) {
										Map.Entry pair = (Map.Entry) it.next();

										String[] lastCharacterOfKey = pair.getKey().toString().split("\\.");

										// Notify NODE_1 and NODE_2 with extend id and new map
										if (lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_1) || lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_2)) {
											String[] addressOfNode1 = ServentSingleton.getInstance().getList().get(pair.getKey()).split(":");

											// Create socket
											try {
												info = Storage.NOTIFY_ALL + " " +
														Storage.ID_MAP + " " +
														Methods.getAddress() + " " +
														ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
														ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

												ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
											} catch (UnknownHostException e) {
												e.printStackTrace();
											}
										} else if (!pair.getKey().toString().equals(parentId) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
											// Notify other parent if exist
											String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
											String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

											// Create socket
											try {
												info = Storage.NOTIFY_ALL + " " +
														Storage.ID + " " +
														Methods.getAddress() + " " +
														ServentSingleton.getInstance().getId();

												ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
											} catch (UnknownHostException e) {
												e.printStackTrace();
											}
										}
									}

									// Print Info
									try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
								}
							}
						} else {
							// Go to local parent
						}

						break;
					case "ACCEPT_NODE":
						String id = null;

						// Update his parent with node parent ip:port
						if (message.length == 4) {
							id = message[3];

							// Set Servant
							ServentSingleton.getInstance().updateList(id, ip + ":" + port);
//							ServentSingleton.getInstance().updateProccess(id, "0");
//							ServentSingleton.getInstance().updateProccess(id, "0." +  (Methods.numberOfChildrenGlobal(id) - Integer.parseInt(ServentSingleton.getInstance().getList().get(id))));

							String node1IdA = Methods.getNode1(ServentSingleton.getInstance().getList());
							String node2IdA = Methods.getNode2(ServentSingleton.getInstance().getList());

							if (node1IdA != null) {
								String[] node1Address = ServentSingleton.getInstance().getList().get(node1IdA).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_CHILD + " " +
											Storage.ACCEPT_NODE + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
											ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

									ServentListener.createSocket(node1Address[0], node1Address[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}

							if (node2IdA != null) {
								String[] node2Address = ServentSingleton.getInstance().getList().get(node2IdA).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_CHILD + " " +
											Storage.ACCEPT_NODE + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
											ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

									ServentListener.createSocket(node2Address[0], node2Address[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}
						} else {
							// create new parent
							String map = message[3];
							String proccess = message[4];
							id = message[5];

							// Set Servant
							ServentSingleton.getInstance().setEmptyLocalChild(2);
							ServentSingleton.getInstance().setId(id);
							ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));
							ServentSingleton.getInstance().setProccess(Methods.createHashMap(Methods.parseHashMap(proccess)));
							try {
								ServentSingleton.getInstance().updateList(id, Methods.getAddress());
								ServentSingleton.getInstance().updateProccess(id, "0" + id.substring(1));
//								ServentSingleton.getInstance().updateProccess(id, "0." +  (Methods.numberOfChildrenGlobal(id) - Integer.parseInt(ServentSingleton.getInstance().getList().get(id))));
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}

							ServentSingleton.getInstance().setResultPlayer1(0);
							ServentSingleton.getInstance().setResultPlayer2(0);
							ServentSingleton.getInstance().setLocalResultPlayer1(0);
							ServentSingleton.getInstance().setLocalResultPlayer2(0);
							ServentSingleton.getInstance().setIzigravanje("0");

							// CIRCLE CHECK
							// Create socket
							info = Storage.CIRCLE_CHECK + " test 123123:3123";
							try {
								ServentListener.createSocket(Methods.getIp(), Integer.toString(Methods.getPort()), info);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}

							String notifyAddressTemp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							String[] notifyAddress;
							if (notifyAddressTemp.contains(" ")) {
								notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
							} else {
								notifyAddress = notifyAddressTemp.split(":");
							}

							// Create socket
							try {
								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.GAME_FINISHED + " " +
										Methods.getAddress()+ " " +
										ServentSingleton.getInstance().getId() + " 1 1";

								ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}
						}

						// Print Info
						try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						break;
					case "FREE_FIELD":
						String childParentId = message[3];
						String childParentFreeFields = message[4];


						// if contains number of freeParent
						if (ServentSingleton.getInstance().getList().containsKey(childParentId)) {

							if (ServentSingleton.getInstance().getList().get(childParentId).contains(" ")) {
								String[] childParentInMap = ServentSingleton.getInstance().getList().get(childParentId).split(" "); // ip:port 4
								String[] parseChildParentInMap = childParentInMap[0].split(":");
								ServentSingleton.getInstance().updateList(childParentId, parseChildParentInMap[0] + ":" + port + " " + (Integer.parseInt(childParentInMap[1]) + Integer.parseInt(childParentFreeFields)));
							}

							String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
							String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());


							if (node1Id != null) {
								String[] node1Address = ServentSingleton.getInstance().getList().get(node1Id).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_CHILD + " " +
											Storage.ACCEPT_NODE + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
											ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

									ServentListener.createSocket(node1Address[0], node1Address[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}

							if (node2Id != null) {
								String[] node2Address = ServentSingleton.getInstance().getList().get(node2Id).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_CHILD + " " +
											Storage.ACCEPT_NODE + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
											ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

									ServentListener.createSocket(node2Address[0], node2Address[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}

							// Notify all other parents
							if (ServentSingleton.getInstance().getId().equals(childParentId)) {
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

										// Create socket
										try {
											info = Storage.NOTIFY_PARENT + " " +
													Storage.ACCEPT_NODE + " " +
													Methods.getAddress() + " " +
													childParentId;

											ServentListener.createSocket(otherLocalParentAddress[0], otherLocalParentAddress[1], info);
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									}
								}
							}

							// If it is not global parent
							if (!Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {

								String parentId = Methods.getParent(ServentSingleton.getInstance().getList());
								String[] addressOfParent = ServentSingleton.getInstance().getList().get(parentId).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_PARENT + " " +
											Storage.FREE_FIELD + " " +
											Methods.getAddress() + " " +
											childParentId + " " +
											childParentFreeFields;

									ServentListener.createSocket(addressOfParent[0], addressOfParent[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}

							// Print Info
							try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						}

						break;
				}
				break;
			case "NOTIFY_CHILD":
				switch (localKey) {
					case "NODE":

						// Create socket
						try {
							info = Storage.NOTIFY_PARENT + " " +
									Storage.NODE + " " +
									Methods.getAddress();

							ServentListener.createSocket(ip, port, info);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						break;
					case "ACCEPT_NODE":

						String list = message[3];
						String proccess = message[4];

						// If accept new node
						if (message.length == 6) {
							String id = message[5];

							// Set Servant
							ServentSingleton.getInstance().setId(id);
							ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(list)));
							ServentSingleton.getInstance().setProccess(Methods.createHashMap(Methods.parseHashMap(proccess)));

							ServentSingleton.getInstance().setResultPlayer1(0);
							ServentSingleton.getInstance().setResultPlayer2(0);
							ServentSingleton.getInstance().setLocalResultPlayer1(0);
							ServentSingleton.getInstance().setLocalResultPlayer2(0);
							ServentSingleton.getInstance().setIzigravanje("0");

							// CIRCLE CHECK
							// Create socket
							info = Storage.CIRCLE_CHECK + " test 123123:3123";
							try {
								ServentListener.createSocket(Methods.getIp(), Integer.toString(Methods.getPort()), info);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}

							String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getLocalParent(ServentSingleton.getInstance().getId()));
							String[] notifyAddress;
							if (notifyAddressTemp.contains(" ")) {
								notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
							} else {
								notifyAddress = notifyAddressTemp.split(":");
							}

							// Create socket
							try {
								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.GAME_FINISHED + " " +
										Methods.getAddress()+ " " +
										ServentSingleton.getInstance().getId() + " 1 0";

								ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}

						} else {
							// If notify old node

							// Set Servant
							ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(list)));
							ServentSingleton.getInstance().setProccess(Methods.createHashMap(Methods.parseHashMap(proccess)));
						}

						// Print Info
						try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						break;
					case "MORE_GAME":

						if (Methods.isNode1(ServentSingleton.getInstance().getId()) || Methods.isNode2(ServentSingleton.getInstance().getId())) {

							if (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId())) > 0) {

								int myTimes = ServentSingleton.getInstance().getTempTimes();
								int hisTimes = myTimes / 2;

								if (Storage.TIMES_MIN_SEND < hisTimes) {
									System.out.println(ServentSingleton.getInstance().getTempTimes() + " - - - " + myTimes);
									ServentSingleton.getInstance().setTempTimes(myTimes - hisTimes);

									try {
										System.out.println("[" + ip + ":" + port + "] => [" + Methods.getAddress() + "] Imao sam " + myTimes + " izigravanja, dao sam " + (myTimes / 2) + " i ostalo mi je " + (myTimes - hisTimes));
									} catch (UnknownHostException e) {
										e.printStackTrace();
									}

									// Create socket
									try {
										info = Storage.NOTIFY_CHILD + " " +
												Storage.ACCEPT_MORE_GAME + " " +
												Methods.getAddress() + " " +
												hisTimes;

										ServentListener.createSocket(ip, port, info);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									}
								} else {
									String[] notifyAddress;
//									String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList()));
									String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getLocalParent(ServentSingleton.getInstance().getId()));
									if (notifyAddressTemp.contains(" ")) {
										notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
									} else {
										notifyAddress = notifyAddressTemp.split(":");
									}

									// Create socket
									info = Storage.NOTIFY_GLOBAL_PARENT + " " +
											Storage.GAME_FINISHED + " " +
											ip + ":" + port + " " +
											message[3] + " 1 0";

									ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);

								}
							} else {
								// vrati nazad

								String[] notifyAddress;
//								String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList()));
								String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getLocalParent(ServentSingleton.getInstance().getId()));
								if (notifyAddressTemp.contains(" ")) {
									notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
								} else {
									notifyAddress = notifyAddressTemp.split(":");
								}

								// Create socket
								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
										Storage.GAME_FINISHED + " " +
										ip + ":" + port + " " +
										message[3] + " 1 0";

								ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
							}
						} else {
							// Ako je on sam slobodan

							Iterator iterateMoreGame;

							// proveri da li neko izigrava
							// NOTIFY_ALL
							iterateMoreGame = ServentSingleton.getInstance().getProccess().entrySet().iterator();
							while (iterateMoreGame.hasNext()) {
								Map.Entry pair = (Map.Entry) iterateMoreGame.next();

								if (pair.getKey().toString().equals(message[4])) {
									continue;
								}

								// Ako nije parent
								if (!pair.getValue().toString().contains(".")) {

									// Ako ima slobodnih
									if (Integer.parseInt(pair.getValue().toString()) > 0) {

										// Ako childovi imaju izigravanja, salji njima note
										if (Methods.isNode1(pair.getKey().toString()) || Methods.isNode2(pair.getKey().toString())) {
//												 Ako njegovi childovi imaju izigrivanja, posalji ih kod njih, da im uzme izigravanja momentalno

											String[] notifyAddress;

											if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
												String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
												notifyAddress = parseToNotifyAddress[0].split(":");
											} else {
												notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
											}

											String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
											String[] testProcesa1;
											if (testProcesa.toString().contains(".")) {
												testProcesa1 = testProcesa.toString().split("\\.");
												if (Methods.isLocalParent(message[3])) {
													System.out.println("Pitaj za izigravanja1.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
												} else {
													System.out.println("Pitaj za izigravanja1.2 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
													ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
												}
											} else {
												System.out.println("Pitaj za izigravanja1.3 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
												ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
											}

											ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());


											// Create socket
											info = Storage.NOTIFY_CHILD + " " +
													Storage.MORE_GAME + " " +
													ip + ":" + port + " " +
													message[3] + " " +
													ServentSingleton.getInstance().getId();

											ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
											break;
										} else {
											// Ako parent ima izigravanja
											int myTimes = ServentSingleton.getInstance().getTempTimes();
											int hisTimes = myTimes / 2;

											if (Storage.TIMES_MIN_SEND < hisTimes) {
												System.out.println(ServentSingleton.getInstance().getTempTimes() + " - - - " + myTimes);
												ServentSingleton.getInstance().setTempTimes(myTimes - hisTimes);

												try {
													System.out.println("[" + ip + ":" + port + "] => [" + Methods.getAddress() + "] Imao sam " + myTimes + " izigravanja, dao sam " + (myTimes / 2) + " i ostalo mi je " + (myTimes - hisTimes));
												} catch (UnknownHostException e) {
													e.printStackTrace();
												}

												String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
												String[] testProcesa1;
												if (testProcesa.toString().contains(".")) {
													testProcesa1 = testProcesa.toString().split("\\.");
													if (Methods.isLocalParent(message[3])) {
														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
													} else {
														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1)) + "] " + ServentSingleton.getInstance().getProccess());
														ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1));
													}
												} else {
													System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
													ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
												}

												// Create socket
												try {
													info = Storage.NOTIFY_CHILD + " " +
															Storage.ACCEPT_MORE_GAME + " " +
															Methods.getAddress() + " " +
															hisTimes;

													ServentListener.createSocket(ip, port, info);
												} catch (UnknownHostException e) {
													e.printStackTrace();
												}
											}
											break;
										}
									}
								} else {
									// Ako je on sam slobodan
									if (Integer.parseInt(pair.getValue().toString().split("\\.")[0]) > 0 || Integer.parseInt(pair.getValue().toString().split("\\.")[1]) > 0) {

										// Ako njegovi childovi imaju izigrivanja, posalji ih kod njih, da im uzme izigravanja momentalno

										String[] notifyAddress;

										if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
											String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
											notifyAddress = parseToNotifyAddress[0].split(":");
										} else {
											notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
										}

										String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
										String[] testProcesa1;
										if (testProcesa.toString().contains(".")) {
											testProcesa1 = testProcesa.toString().split("\\.");
											if (Methods.isLocalParent(message[3])) {
												System.out.println("Pitaj za izigravanja1.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
												ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
											} else {
												System.out.println("Pitaj za izigravanja1.2 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 )) + "] " + ServentSingleton.getInstance().getProccess());
												ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1 ));
											}
										} else {
											System.out.println("Pitaj za izigravanja1.3 " + notifyAddress[0] + ":" + notifyAddress[1] + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
											ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
										}

										ServentSingleton.getInstance().getWaiting().put(message[3], pair.getKey().toString());

										// Create socket
										info = Storage.NOTIFY_CHILD + " " +
												Storage.MORE_GAME + " " +
												ip + ":" + port + " " +
												message[3] + " " +
												ServentSingleton.getInstance().getId();

										ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);

										break;
									}
								}
							}
//							if (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId())) > 0) {
//
//								// uzmi od njega iako je parent jer izigrava
//								if (ServentSingleton.getInstance().isPlaying()) {
//									int myTimes = ServentSingleton.getInstance().getTempTimes();
//									int hisTimes = myTimes / 2;
//
//									if (Storage.TIMES_MIN_SEND < hisTimes) {
//										System.out.println(ServentSingleton.getInstance().getTempTimes() + " - - - " + myTimes);
//										ServentSingleton.getInstance().setTempTimes(myTimes - hisTimes);
//
//										try {
//											System.out.println("[" + ip + ":" + port + "] => [" + Methods.getAddress() + "] Imao sam " + myTimes + " izigravanja, dao sam " + (myTimes / 2) + " i ostalo mi je " + (myTimes - hisTimes));
//										} catch (UnknownHostException e) {
//											e.printStackTrace();
//										}
//
//										// Create socket
//										try {
//											info = Storage.NOTIFY_CHILD + " " +
//													Storage.ACCEPT_MORE_GAME + " " +
//													Methods.getAddress() + " " +
//													hisTimes;
//
//											ServentListener.createSocket(ip, port, info);
//										} catch (UnknownHostException e) {
//											e.printStackTrace();
//										}
//
//									} else {
//										String[] notifyAddress;
//										String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList()));
//										if (notifyAddressTemp.contains(" ")) {
//											notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
//										} else {
//											notifyAddress = notifyAddressTemp.split(":");
//										}
//
//										// Create socket
//										info = Storage.NOTIFY_GLOBAL_PARENT + " " +
//												Storage.GAME_FINISHED + " " +
//												ip + ":" + port + " " +
//												message[3] + " 1";
//
//										ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//
//									}
//								} else {
//									// proveri da li neko izigrava
//									// NOTIFY_ALL
//									Iterator iterateMoreGame = ServentSingleton.getInstance().getProccess().entrySet().iterator();
//									while (iterateMoreGame.hasNext()) {
//										Map.Entry pair = (Map.Entry) iterateMoreGame.next();
//
//										// Ako izigrava
//										if (!ServentSingleton.getInstance().getProccess().get(pair.getValue().toString()).contains(".")) {
//											if (Integer.parseInt(pair.getValue().toString()) > 0) {
//
//												// ne proveravaj onog ko ti je poslao
//												if (!message[4].equals(pair.getKey().toString()) && !ServentSingleton.getInstance().getId().equals(pair.getKey().toString())) {
//
//													// Ako njegovi childovi imaju izigrivanja, posalji ih kod njih, da im uzme izigravanja momentalno
//													if (Methods.isNode1(pair.getKey().toString()) || Methods.isNode2(pair.getKey().toString())) {
//
//														String[] notifyAddress;
//
//														if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//															String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//															notifyAddress = parseToNotifyAddress[0].split(":");
//														} else {
//															notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//														}
//
//														// Create socket
//														info = Storage.NOTIFY_CHILD + " " +
//																Storage.MORE_GAME + " " +
//																ip + ":" + port + " " +
//																message[3];
//
//														ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//
//														break;
//													} else {
//														// ako je neki drugi parent a ima izigravanja neko njegov, salji ga kod njega !
//														String[] notifyAddress;
//
//														if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//															String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//															notifyAddress = parseToNotifyAddress[0].split(":");
//														} else {
//															notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//														}
//
//														// Create socket
//														info = Storage.NOTIFY_CHILD + " " +
//																Storage.MORE_GAME + " " +
//																ip + ":" + port + " " +
//																message[3] + " " +
//																ServentSingleton.getInstance().getId();
//
//														ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//
//														break;
//													}
//												}
//											}
//										} else {
//											if (Integer.parseInt(pair.getValue().toString().split("\\.")[0]) > 0) {
//
//												if (!message[4].equals(pair.getKey().toString()) && !ServentSingleton.getInstance().getId().equals(pair.getKey().toString())) {
//													// ako je neki drugi parent a ima izigravanja neko njegov, salji ga kod njega !
//													String[] notifyAddress;
//
//													if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//														String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//														notifyAddress = parseToNotifyAddress[0].split(":");
//													} else {
//														notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//													}
//
//
//													//												System.out.println("Pitaj za izigravanja5.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
//													//												ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//
//													String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//													String[] testProcesa1;
//													if (testProcesa.toString().contains(".")) {
//														testProcesa1 = testProcesa.toString().split("\\.");
//														if (Methods.isLocalParent(message[3])) {
//															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//														} else {
//															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1)) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1));
//														}
//													} else {
//														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//													}
//
//													// Create socket
//													info = Storage.NOTIFY_CHILD + " " +
//															Storage.MORE_GAME + " " +
//															ip + ":" + port + " " +
//															message[3] + " " +
//															ServentSingleton.getInstance().getId();
//
//													ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//													break;
//												}
//											} else if (Integer.parseInt(pair.getValue().toString().split("\\.")[1]) > 0) {
//												if (!message[4].equals(pair.getKey().toString()) && !ServentSingleton.getInstance().getId().equals(pair.getKey().toString())) {
//													// ako je neki drugi parent a ima izigravanja neko njegov, salji ga kod njega !
//													String[] notifyAddress;
//
//													if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
//														String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
//														notifyAddress = parseToNotifyAddress[0].split(":");
//													} else {
//														notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
//													}
//
//
////												System.out.println("Pitaj za izigravanja6.1 " + notifyAddress[0] + ":" + notifyAddress[1] + " - - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "]");
////												ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//
//													String testProcesa = ServentSingleton.getInstance().getProccess().get(message[3]);
//													String[] testProcesa1;
//													if (testProcesa.toString().contains(".")) {
//														testProcesa1 = testProcesa.toString().split("\\.");
//														if (Methods.isLocalParent(message[3])) {
//															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(testProcesa1[0]) + 1) + "." + testProcesa1[1]);
//														} else {
//															System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + (testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1)) + "] " + ServentSingleton.getInstance().getProccess());
//															ServentSingleton.getInstance().getProccess().put(message[3], testProcesa1[0] + "." + Integer.toString(Integer.parseInt(testProcesa1[1]) + 1));
//														}
//													} else {
//														System.out.println("Posalji izigravanja " + ip + ":" + port + " - " + message[3] + " - - [" + ServentSingleton.getInstance().getProccess().get(message[3]) + "] => [" + Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1) + "] " + ServentSingleton.getInstance().getProccess());
//														ServentSingleton.getInstance().getProccess().put(message[3], Integer.toString(Integer.parseInt(ServentSingleton.getInstance().getProccess().get(message[3])) + 1));
//													}
//
//													// Create socket
//													info = Storage.NOTIFY_CHILD + " " +
//															Storage.MORE_GAME + " " +
//															ip + ":" + port + " " +
//															message[3] + " " +
//															ServentSingleton.getInstance().getId();
//
//													ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//													break;
//												}
//											}
//										}
//									}
//								}
//							} else {
//								// vrati nazad
//
//								String[] notifyAddress;
//								String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList()));
//								if (notifyAddressTemp.contains(" ")) {
//									notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
//								} else {
//									notifyAddress = notifyAddressTemp.split(":");
//								}
//
//								// Create socket
//								info = Storage.NOTIFY_GLOBAL_PARENT + " " +
//										Storage.GAME_FINISHED + " " +
//										ip + ":" + port + " " +
//										message[3] + " 1";
//
//								ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
//
//							}
						}
						break;
					case "ACCEPT_MORE_GAME":
//						System.out.println(ip + ":" + port + " " + message[3]);

						if (!message[3].equals("0")) {
							try {
								System.out.println("[" + Methods.getAddress() + "] => [" + ip + ":" + port + "] Uzeo sam " + message[3]);
							} catch (UnknownHostException e) {
								e.printStackTrace();
							}
							ServentSingleton.getInstance().setTempTimes(Integer.parseInt(message[3]));

							// Start game
							GameListener gameListener = new GameListener();
							gameListener.respond();

							Methods.setProccessOn(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().setPlaying(true);
						} else {
							System.out.println("Usla nula, nista ne radimo!");

							String[] notifyAddress;
							String notifyAddressTemp = ServentSingleton.getInstance().getList().get(Methods.getParent(ServentSingleton.getInstance().getList()));
							if (notifyAddressTemp.contains(" ")) {
								notifyAddress = notifyAddressTemp.split(" ")[0].split(":");
							} else {
								notifyAddress = notifyAddressTemp.split(":");
							}

							// Create socket
							info = Storage.NOTIFY_GLOBAL_PARENT + " " +
									Storage.GAME_FINISHED + " " +
									ip + ":" + port + " " +
									message[3] + " 1 0";

							ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
						}
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
						Methods.extendHashMap(ServentSingleton.getInstance().getProccess());

						// NOTIFY_ALL
						Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pair = (Map.Entry) it.next();

							String[] lastCharacterOfKey = pair.getKey().toString().split("\\.");

							// Notify NODE_1 and NODE_2 with extend id and new map
							if (lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_1) || lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_2)) {
								String[] addressOfNode1 = ServentSingleton.getInstance().getList().get(pair.getKey()).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_ALL + " " +
											Storage.ID_MAP + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
											ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

									ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							} else if (!pair.getKey().toString().equals(parentId) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
								// Notify other parent if exist
								String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
								String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_ALL + " " +
											Storage.ID + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getId();

									ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}
						}

						// Print Info
						try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						break;
					case "ID_MAP":
						// CHILD NOTIFY

						String map = message[3];
						String proccess = message[4];

						// Set Servant
						// Extend ID
						ServentSingleton.getInstance().setId("0." + ServentSingleton.getInstance().getId());
						// Set new map from parent
						ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(map)));
						ServentSingleton.getInstance().setProccess(Methods.createHashMap(Methods.parseHashMap(proccess)));

						// Print Info
						try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						break;
					case "ID_DOWN":
						// DECREASE IDS

						String parentId1 = message[3];

						// Set Servant
						// Reduce ID
						if (ServentSingleton.getInstance().getId().contains(".")) {
							if (ServentSingleton.getInstance().getId().split("\\.")[0].equals("1")) {
								ServentSingleton.getInstance().setId(ServentSingleton.getInstance().getId().substring(0, 2) + ServentSingleton.getInstance().getId().substring(4));
							} else {
								ServentSingleton.getInstance().setId(ServentSingleton.getInstance().getId().substring(2));
							}
						}
						// Extend map
						Methods.reduceHashMap(ServentSingleton.getInstance().getList());
						Methods.reduceHashMap(ServentSingleton.getInstance().getProccess());

						// NOTIFY_ALL
						Iterator ite = ServentSingleton.getInstance().getList().entrySet().iterator();
						while (ite.hasNext()) {
							Map.Entry pair = (Map.Entry) ite.next();

							String[] lastCharacterOfKey = pair.getKey().toString().split("\\.");

							// Notify NODE_1 and NODE_2 with extend id and new map
							if (lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_1) || lastCharacterOfKey[lastCharacterOfKey.length - 1].equals(Storage.NODE_2)) {
								String[] addressOfNode1 = ServentSingleton.getInstance().getList().get(pair.getKey()).split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_ALL + " " +
											Storage.ID_DOWN_MAP + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getList().toString().replace(" ", "--") + " " +
											ServentSingleton.getInstance().getProccess().toString().replace(" ", "--");

									ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							} else if (!pair.getKey().toString().equals(parentId1) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
								// Notify other parent if exist
								String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
								String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

								// Create socket
								try {
									info = Storage.NOTIFY_ALL + " " +
											Storage.ID_DOWN + " " +
											Methods.getAddress() + " " +
											ServentSingleton.getInstance().getId();

									ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);
								} catch (UnknownHostException e) {
									e.printStackTrace();
								}
							}
						}

						// Print Info
						try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						break;

					case "ID_DOWN_MAP":
						// CHILD NOTIFY

						String mapReduce = message[3];
						String proccessReduce = message[3];

						// Set Servant
						// Reduce ID
						if (ServentSingleton.getInstance().getId().contains(".")) {
							if (ServentSingleton.getInstance().getId().split("\\.")[0].equals("1")) {
								ServentSingleton.getInstance().setId(ServentSingleton.getInstance().getId().substring(0, 2) + ServentSingleton.getInstance().getId().substring(4));
							} else {
								ServentSingleton.getInstance().setId(ServentSingleton.getInstance().getId().substring(2));
							}
						}

						// Set new map from parent
						ServentSingleton.getInstance().setList(Methods.createHashMap(Methods.parseHashMap(mapReduce)));
						ServentSingleton.getInstance().setProccess(Methods.createHashMap(Methods.parseHashMap(proccessReduce)));

						// Print Info
						try { Methods.printInfo(); } catch (UnknownHostException e) { e.printStackTrace(); }
						break;
					case "GAME":

						// Check if new game started!
						if (!ServentSingleton.getInstance().getIzigravanje().equals(message[5])) {
							System.out.println("NOVA IGRA ALL");
							System.out.println("--------------------------------------------------");
							ServentSingleton.getInstance().setResultPlayer1(0);
							ServentSingleton.getInstance().setResultPlayer2(0);
							ServentSingleton.getInstance().setLocalResultPlayer1(0);
							ServentSingleton.getInstance().setLocalResultPlayer2(0);
							ServentSingleton.getInstance().setIzigravanje(message[5]);
						}

						String game = message[3];

						String gameParentId = message[4];

						String[] parseOption = game.split(":");
						String player1 = parseOption[0];
						String player2 = parseOption[1];
						int row = Integer.parseInt(parseOption[2]);
						int col = Integer.parseInt(parseOption[3]);
						int tokens = Integer.parseInt(parseOption[4]);
						int times = Integer.parseInt(parseOption[5]);
						int tempTimes;
						int timesDiv;

						if (Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {
							timesDiv = Methods.getTimesForEachNode(ServentSingleton.getInstance().getList());
							tempTimes = times / timesDiv; // 1000 / 3 = > 333
							tempTimes += times % timesDiv; // 334
						} else {
							tempTimes = Integer.parseInt(parseOption[6]);
							timesDiv = Integer.parseInt(parseOption[7]);
						}

						ServentSingleton.getInstance().setPlayer1(player1);
						ServentSingleton.getInstance().setPlayer2(player2);
						ServentSingleton.getInstance().setRow(row);
						ServentSingleton.getInstance().setCol(col);
						ServentSingleton.getInstance().setTokens(tokens);
						ServentSingleton.getInstance().setTimes(times);

//						if (Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {
//							ServentSingleton.getInstance().setTempTimes(times);
//						} else {
							ServentSingleton.getInstance().setTempTimes(tempTimes);
//						}

						// Start game
						GameListener gameListener = new GameListener();
						gameListener.respond();

						if (Methods.isGlobalParent(ServentSingleton.getInstance().getId())) {
							tempTimes -= times % timesDiv;
						}


						Methods.setProccessOn(ServentSingleton.getInstance().getId());
						ServentSingleton.getInstance().setPlaying(true);

						try {
							Methods.printInfo();
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}

						// NOTIFY_ALL
						if (!Methods.isNode1(ServentSingleton.getInstance().getId()) && !Methods.isNode2(ServentSingleton.getInstance().getId())) {
							Iterator iter = ServentSingleton.getInstance().getList().entrySet().iterator();
							while (iter.hasNext()) {
								Map.Entry pair = (Map.Entry) iter.next();

								// Not send to same node which call case
								if (!pair.getKey().toString().equals(gameParentId)) {
									String[] notifyAddress;

									if (ServentSingleton.getInstance().getList().get(pair.getKey().toString()).contains(" ")) {
										String[] parseToNotifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
										notifyAddress = parseToNotifyAddress[0].split(":");
									} else {
										notifyAddress = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(":");
									}

									if (Methods.isNode1(pair.getKey().toString()) && !ServentSingleton.getInstance().getId().equals(pair.getKey().toString())) {

										// Create socket
										try {
											info = Storage.NOTIFY_ALL + " " +
													Storage.GAME + " " +
													Methods.getAddress() + " " +
													player1 + ":" + player2 + ":" + row + ":" + col + ":" + tokens + ":" + times + ":" + times + ":" + timesDiv + " " +
													ServentSingleton.getInstance().getId() + " " +
													message[5];

											ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);

											Methods.setProccessOn(pair.getKey().toString());
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									} else if (Methods.isNode2(pair.getKey().toString()) && !ServentSingleton.getInstance().getId().equals(pair.getKey().toString())) {

										// Create socket
										try {
											info = Storage.NOTIFY_ALL + " " +
													Storage.GAME + " " +
													Methods.getAddress() + " " +
													player1 + ":" + player2 + ":" + row + ":" + col + ":" + tokens + ":" + times + ":" + times + ":" + timesDiv + " " +
													ServentSingleton.getInstance().getId() + " " +
													message[5];

											ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);

											Methods.setProccessOn(pair.getKey().toString());
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									} else if (!pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {
										// Notify other parent if exist
										String[] parseAddressAndFreeFields = ServentSingleton.getInstance().getList().get(pair.getKey().toString()).split(" ");
										String[] addressOfNode1 = parseAddressAndFreeFields[0].split(":");

										// Create socket
										try {
											info = Storage.NOTIFY_ALL + " " +
													Storage.GAME + " " +
													Methods.getAddress() + " " +
													player1 + ":" + player2 + ":" + row + ":" + col + ":" + tokens + ":" + times + ":" + times + ":" + timesDiv + " " +
													ServentSingleton.getInstance().getId() + " " +
													message[5];

											ServentListener.createSocket(addressOfNode1[0], addressOfNode1[1], info);

											Methods.setProccessOn(pair.getKey().toString());
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									}
								}

							}
						}
						break;
					case "WIN":

//						System.out.println("[PARENT] Javio mi se: " + message[3] + ", sa " + message[5]);

						String[] notifyAddress;
						String[] winnersChildParent = message[5].split(":");

						if (ServentSingleton.getInstance().getIzigravanje().equals(message[4])) {
							ServentSingleton.getInstance().setResultPlayer1(ServentSingleton.getInstance().getResultPlayer1() + Integer.parseInt(winnersChildParent[0]));
							ServentSingleton.getInstance().setResultPlayer2(ServentSingleton.getInstance().getResultPlayer2() + Integer.parseInt(winnersChildParent[1]));
						} else {
							System.out.println("NOVA IGRA WIN");
							System.out.println("--------------------------------------------------");
							ServentSingleton.getInstance().setResultPlayer1(Integer.parseInt(winnersChildParent[0]));
							ServentSingleton.getInstance().setResultPlayer2(Integer.parseInt(winnersChildParent[1]));
						}

						// Print Time
//						Methods.printLoadTime(ServentSingleton.getInstance().getResultPlayer1(), ServentSingleton.getInstance().getResultPlayer2(), ServentSingleton.getInstance().getTimes(), ServentSingleton.getInstance().getLoadTime());


						String caseWin;
						String subcaseWin;
						String addressWin;
						String idWin;
						String izigravanjeWin;
						String gamesWin;
						String timeWin;

						Iterator itera = ServentSingleton.getInstance().getList().entrySet().iterator();
						while (itera.hasNext()) {
							Map.Entry pair = (Map.Entry) itera.next();


							// Ne javljaj samom sebi i onome od koga si dobio poruku
							if (!pair.getKey().toString().equals(ServentSingleton.getInstance().getId()) && !pair.getKey().toString().equals(message[3])) {

								// Ako nisam od childa dobio poruku, smem i childovima
								if (!Methods.isNode1(message[3]) && !Methods.isNode2(message[3])) {


									if (Methods.isNode1(pair.getKey().toString()) || Methods.isNode2(pair.getKey().toString())) {
										if (pair.getValue().toString().contains(" ")) {
											notifyAddress = pair.getValue().toString().split(" ")[0].split(":");
										} else {
											notifyAddress = pair.getValue().toString().split(":");
										}

										// Create socket
										try {
											info = Storage.NOTIFY_ALL + " " +
													Storage.WIN_CHILD + " " +
													Methods.getAddress() + " " +
													ServentSingleton.getInstance().getId() + " " +
													ServentSingleton.getInstance().getIzigravanje() + " " +
													winnersChildParent[0] + ":" + winnersChildParent[1] + " " +
													message[6];

											ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									} else {
										if (pair.getValue().toString().contains(" ")) {
											notifyAddress = pair.getValue().toString().split(" ")[0].split(":");
										} else {
											notifyAddress = pair.getValue().toString().split(":");
										}

										// Create socket
										try {
											info = Storage.NOTIFY_ALL + " " +
													Storage.WIN + " " +
													Methods.getAddress() + " " +
													ServentSingleton.getInstance().getId() + " " +
													ServentSingleton.getInstance().getIzigravanje() + " " +
													winnersChildParent[0] + ":" + winnersChildParent[1] + " " +
													message[6];

											ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
										} catch (UnknownHostException e) {
											e.printStackTrace();
										}
									}

								} else {
									// Ako sam dobio od childa poruku, ne javljam drugom childu


									// Ako je na redu child, preskoci
									if (Methods.isNode1(pair.getKey().toString()) || Methods.isNode2(pair.getKey().toString())) {
										continue;
									}

									if (pair.getValue().toString().contains(" ")) {
										notifyAddress = pair.getValue().toString().split(" ")[0].split(":");
									} else {
										notifyAddress = pair.getValue().toString().split(":");
									}

									// Create socket
									try {
										info = Storage.NOTIFY_ALL + " " +
												Storage.WIN + " " +
												Methods.getAddress() + " " +
												ServentSingleton.getInstance().getId() + " " +
												ServentSingleton.getInstance().getIzigravanje() + " " +
												winnersChildParent[0] + ":" + winnersChildParent[1] + " " +
												message[6];

										ServentListener.createSocket(notifyAddress[0], notifyAddress[1], info);
									} catch (UnknownHostException e) {
										e.printStackTrace();
									}
								}
							}
						}
						break;
					case "WIN_CHILD":

						// Print Time
//						Methods.printLoadTime(ServentSingleton.getInstance().getResultPlayer1(), ServentSingleton.getInstance().getResultPlayer2(), ServentSingleton.getInstance().getTimes(), ServentSingleton.getInstance().getLoadTime());

						String[] winnersChildNotify = message[5].split(":");
						if (ServentSingleton.getInstance().getIzigravanje().equals(message[4])) {
							ServentSingleton.getInstance().setResultPlayer1(ServentSingleton.getInstance().getResultPlayer1() + Integer.parseInt(winnersChildNotify[0]));
							ServentSingleton.getInstance().setResultPlayer2(ServentSingleton.getInstance().getResultPlayer2() + Integer.parseInt(winnersChildNotify[1]));
						} else {
							System.out.println("NOVA IGRA WIN CHILD");
							System.out.println("--------------------------------------------------");
							ServentSingleton.getInstance().setIzigravanje(message[4]);
							ServentSingleton.getInstance().setResultPlayer1(Integer.parseInt(winnersChildNotify[0]));
							ServentSingleton.getInstance().setResultPlayer2(Integer.parseInt(winnersChildNotify[1]));
						}

						break;
				}
				break;
			case "CIRCLE_CHECK":

				String[] freeFieldAddress = null;
				String freeFieldId = null;
				int freeFieldNumber = -1;

				// if it is parent
				if (ServentSingleton.getInstance().getId() != null && Methods.isLocalParent(ServentSingleton.getInstance().getId())) {
					String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());
					String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());

					isAvailableAddressNode1 = node1Id != null && ServentSingleton.getInstance().getList().containsKey(node1Id) ? ServentSingleton.getInstance().getList().get(node1Id).split(":") : null;
					isAvailableAddressNode2 = node2Id != null && ServentSingleton.getInstance().getList().containsKey(node2Id) ? ServentSingleton.getInstance().getList().get(node2Id).split(":") : null;

					// if NODE_2 exist and is NOT alive
					if (node2Id != null && isAvailableAddressNode2 != null && ServentListener.isDead(isAvailableAddressNode2[0], Integer.parseInt(isAvailableAddressNode2[1]))) {

						// if NODE_1 exist and is NOT alive
						if (node1Id != null && isAvailableAddressNode1 != null && ServentListener.isDead(isAvailableAddressNode1[0], Integer.parseInt(isAvailableAddressNode1[1]))) {

							System.out.println("Node : 1 [DEAD] " + isAvailableAddressNode1[0] + ":" + isAvailableAddressNode1[1]);
							System.out.println("Node : 2 [DEAD] " + isAvailableAddressNode2[0] + ":" + isAvailableAddressNode2[1]);

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(node1Id);
							ServentSingleton.getInstance().getList().remove(node2Id);

							ServentSingleton.getInstance().getProccess().remove(node1Id);
							ServentSingleton.getInstance().getProccess().remove(node2Id);

							ServentSingleton.getInstance().setEmptyLocalChild(2);

							// Notify parent
							freeFieldAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
							freeFieldId = ServentSingleton.getInstance().getId();
							freeFieldNumber = 2;

						} else if (isAvailableAddressNode2 != null) {
							System.out.println("Node : 2 [DEAD] " + isAvailableAddressNode2[0] + ":" + isAvailableAddressNode2[1]);

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(node2Id);

							ServentSingleton.getInstance().getProccess().remove(node2Id);

							if (node1Id != null) {
								ServentSingleton.getInstance().setEmptyLocalChild(1);
							} else {
								ServentSingleton.getInstance().setEmptyLocalChild(2);
							}

							// Notify parent
							freeFieldAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
							freeFieldId = ServentSingleton.getInstance().getId();
							freeFieldNumber = 1;
						}
					} else if (node1Id != null && isAvailableAddressNode1 != null && ServentListener.isDead(isAvailableAddressNode1[0], Integer.parseInt(isAvailableAddressNode1[1]))) {
						// if NODE_1 exist and is NOT alive

						System.out.println("Node : 1 [DEAD] " + isAvailableAddressNode1[0] + ":" + isAvailableAddressNode1[1]);

						// Set Servant
						// Update map
						ServentSingleton.getInstance().getList().remove(node1Id);

						ServentSingleton.getInstance().getProccess().remove(node1Id);

						if (node2Id != null) {
							ServentSingleton.getInstance().setEmptyLocalChild(1);
						} else {
							ServentSingleton.getInstance().setEmptyLocalChild(2);
						}

						// Notify parent
						freeFieldAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
						freeFieldId = ServentSingleton.getInstance().getId();
						freeFieldNumber = 1;
					} else {
						// if parent is removed, update all ids
						Iterator it = ServentSingleton.getInstance().getList().entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pair = (Map.Entry) it.next();

							if (Methods.isLocalParent(pair.getKey().toString()) && !Methods.isGlobalParent(pair.getKey().toString()) && !pair.getKey().toString().equals(ServentSingleton.getInstance().getId())) {

								isAvailableAddress = ServentSingleton.getInstance().getList().containsKey(pair.getKey()) ? ServentSingleton.getInstance().getList().get(pair.getKey()).split(" ")[0].split(":") : null;

								if (isAvailableAddress != null && ServentListener.isDead(isAvailableAddress[0], Integer.parseInt(isAvailableAddress[1]))) {

									// Wait! Maybe is not DEAD!!!

									try {
										Thread.sleep(2500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}

									// Check again
									if (ServentListener.isDead(isAvailableAddress[0], Integer.parseInt(isAvailableAddress[1]))) {

										// Check if key is still in map
										if (ServentSingleton.getInstance().getList().containsKey(pair.getKey())) {
											System.out.println("loseee " + isAvailableAddress[0] + ":" + isAvailableAddress[1]);

											// Notify parent
											String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

											// Set Servant
											// Update map
											ServentSingleton.getInstance().getList().remove(pair.getKey());

											ServentSingleton.getInstance().getProccess().remove(pair.getKey());

											// Create socket
											try {
												info = Storage.NOTIFY_ALL + " " +
														Storage.ID_DOWN + " " +
														Methods.getAddress() + " " +
														ServentSingleton.getInstance().getId();

												ServentListener.createSocket(addressOfParent[0], addressOfParent[1], info);
											} catch (UnknownHostException e) {
												e.printStackTrace();
											}
										}
									}
								}
							}
						}
					}
				} else if (ServentSingleton.getInstance().getId() != null && Methods.isNode1(ServentSingleton.getInstance().getId())) {
					// If it is NODE_1

					String parentId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + "0";
					String node2Id = Methods.getNode2(ServentSingleton.getInstance().getList());

					isAvailableAddressParent = parentId != null && ServentSingleton.getInstance().getList().containsKey(parentId) ? ServentSingleton.getInstance().getList().get(parentId).split(":") : null;
					isAvailableAddressNode2 = node2Id != null && ServentSingleton.getInstance().getList().containsKey(node2Id) ? ServentSingleton.getInstance().getList().get(node2Id).split(":") : null;

					// if Parent exist and is NOT alive
					if (parentId != null && isAvailableAddressParent != null && ServentListener.isDead(isAvailableAddressParent[0], Integer.parseInt(isAvailableAddressParent[1]))) {

						// if NODE_2 exist and is NOT alive
						if (node2Id != null && isAvailableAddressNode2 != null && ServentListener.isDead(isAvailableAddressNode2[0], Integer.parseInt(isAvailableAddressNode2[1]))) {

							System.out.println("Parent : [DEAD] " + isAvailableAddressParent[0] + ":" + isAvailableAddressParent[1]);
							System.out.println("Node : 2 [DEAD] " + isAvailableAddressNode2[0] + ":" + isAvailableAddressNode2[1]);

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(parentId);
							ServentSingleton.getInstance().getList().remove(node2Id);

							ServentSingleton.getInstance().getProccess().remove(parentId);
							ServentSingleton.getInstance().getProccess().remove(node2Id);

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							String temp1 = ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getProccess().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);
							ServentSingleton.getInstance().setEmptyLocalChild(2);

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);
							ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), temp1);


							// Notify parent
							freeFieldAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
							freeFieldId = ServentSingleton.getInstance().getId();
							freeFieldNumber = 2;
						} else if (isAvailableAddressParent != null) {

							System.out.println("Parent : [DEAD] " + isAvailableAddressParent[0] + ":" + isAvailableAddressParent[1]);

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(parentId);
							ServentSingleton.getInstance().getProccess().remove(parentId);

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							String temp1 = ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getProccess().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);
							if (node2Id != null) {
								ServentSingleton.getInstance().setEmptyLocalChild(1);
							} else {
								ServentSingleton.getInstance().setEmptyLocalChild(2);
							}

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);
							ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), temp1);

							// Notify parent
							freeFieldAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
							freeFieldId = ServentSingleton.getInstance().getId();
							freeFieldNumber = 1;
						}
					}
				} else if (ServentSingleton.getInstance().getId() != null && Methods.isNode2(ServentSingleton.getInstance().getId())) {
					// If it is NODE_2

					String parentId = ServentSingleton.getInstance().getId().substring(0, ServentSingleton.getInstance().getId().length() - 1) + "0";
					String node1Id = Methods.getNode1(ServentSingleton.getInstance().getList());

					isAvailableAddressParent = parentId != null && ServentSingleton.getInstance().getList().containsKey(parentId) ? ServentSingleton.getInstance().getList().get(parentId).split(":") : null;
					isAvailableAddressNode1 = node1Id != null && ServentSingleton.getInstance().getList().containsKey(node1Id) ? ServentSingleton.getInstance().getList().get(node1Id).split(":") : null;

					// if Parent exist and is NOT alive
					if (node1Id != null && isAvailableAddressNode1 != null && ServentListener.isDead(isAvailableAddressNode1[0], Integer.parseInt(isAvailableAddressNode1[1]))) {

						// if NODE_2 exist and is NOT alive
						if (parentId != null && isAvailableAddressParent != null && ServentListener.isDead(isAvailableAddressParent[0], Integer.parseInt(isAvailableAddressParent[1]))) {

							System.out.println("Parent : [DEAD] " + isAvailableAddressParent[0] + ":" + isAvailableAddressParent[1]);
							System.out.println("Node : 1 [DEAD] " + isAvailableAddressNode1[0] + ":" + isAvailableAddressNode1[1]);

							// Set Servant
							// Update map
							ServentSingleton.getInstance().getList().remove(parentId);
							ServentSingleton.getInstance().getList().remove(node1Id);

							ServentSingleton.getInstance().getProccess().remove(parentId);
							ServentSingleton.getInstance().getProccess().remove(node1Id);

							ServentSingleton.getInstance().setEmptyLocalChild(2);

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							String temp1 = ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getProccess().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);
							ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), temp1);


							// Notify parent
							freeFieldAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
							freeFieldId = ServentSingleton.getInstance().getId();
							freeFieldNumber = 2;
						}
					} else if(parentId != null && isAvailableAddressParent != null && ServentListener.isDead(isAvailableAddressParent[0], Integer.parseInt(isAvailableAddressParent[1]))) {

						// if NODE_2 exist and is NOT alive

						// Set Servant
						// Update map
						ServentSingleton.getInstance().getList().remove(parentId);

						ServentSingleton.getInstance().getProccess().remove(parentId);

						if (node1Id == null) {

							System.out.println("Parent : [DEAD] " + isAvailableAddressParent[0] + ":" + isAvailableAddressParent[1]);

							if (node1Id != null) {
								ServentSingleton.getInstance().setEmptyLocalChild(1);
							} else {
								ServentSingleton.getInstance().setEmptyLocalChild(2);
							}

							String temp = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId());
							String temp1 = ServentSingleton.getInstance().getProccess().get(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getList().remove(ServentSingleton.getInstance().getId());
							ServentSingleton.getInstance().getProccess().remove(ServentSingleton.getInstance().getId());

							ServentSingleton.getInstance().setId(parentId);

							ServentSingleton.getInstance().getList().put(ServentSingleton.getInstance().getId(), temp);
							ServentSingleton.getInstance().getProccess().put(ServentSingleton.getInstance().getId(), temp1);

							// Notify parent
							freeFieldAddress = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");
							freeFieldId = ServentSingleton.getInstance().getId();
							freeFieldNumber = 1;
						}
					}
				}

				// obavesti sve koje treba
				if (freeFieldAddress != null && freeFieldId != null && freeFieldNumber != -1) {

					// Create socket
					try {
						info = Storage.NOTIFY_PARENT + " " +
								Storage.FREE_FIELD + " " +
								Methods.getAddress() + " " +
								freeFieldId + " " +
								freeFieldNumber;

						ServentListener.createSocket(freeFieldAddress[0], freeFieldAddress[1], info);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}

				// sacekaj 2s posle svake iteracije
				try {
					Thread.sleep(2000);
				} catch	(InterruptedException e) {
					e.printStackTrace();
				}

				// proveri opet da li neko fali, ZA SVAKOG CVORA SE PROLAZI
				String[] addressOfParent = ServentSingleton.getInstance().getList().get(ServentSingleton.getInstance().getId()).split(":");

				// Create socket
				info = Storage.CIRCLE_CHECK + " test 123123:3123";
				try {
					ServentListener.createSocket(Methods.getIp(), Integer.toString(Methods.getPort()), info);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

				break;
			default:
				System.out.println("Wrong communication.");
				break;
		}
	}
}
