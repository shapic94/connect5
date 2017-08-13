package global;

public class Storage {
    // Game
    public static final int ADD_FULL = 1;
    public static final int ADD_FOUNDED = 2;
    public static final int ADD_NEXT = 3;

    public static final int COUNT = 2;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    // Bootstrap & servants communication

    // Server port
    public static final String BOOTSTRAP_IP = "127.0.0.1";
    public static final int BOOTSTRAP_PORT = 1234;

    // New Node ---- > Server
    // NEW port
    public static final String NEW = "NEW";

    // Server ---- > New Node ( if he is first )
    // FIRST ip:port (ip:port from New Node)
    public static final String FIRST = "FIRST";

    // Server ---- > New Node ( if he is not first )
    // NOT_FIRST ip:port (ip:port from random Node)
    public static final String NOT_FIRST = "NOT_FIRST";

    // New Node ---- > Node ( show me where to go )
    // NEW_INFO ip:port
    public static final String NEW_INFO = "NEW_INFO";

    // Node ---- > New Node ( if he is accepted )
    // NEW_ACCEPT ip:port
    public static final String NEW_ACCEPT = "NEW_ACCEPT";

    // New Node ---- > Node ( if i am accepted, tell another child )
    // NEW_ARRIVED ip:port
    public static final String NEW_ARRIVED = "NEW_ARRIVED";

    // Comunication
    // Node
    // key      id      address     port
    // current  0,1,2   address     port
}
