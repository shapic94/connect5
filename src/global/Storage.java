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

    // GUI ---- > Server
    // GUI_INFO
    public static final String GUI_INFO = "GUI_INFO";

    // Parent ---- > Nodes
    public static final String PLAY = "PLAY";

    // Comunication
    // Node
    // key      id      address     port
    // current  0,1,2   address     port


    public static final String NOTIFY_GLOBAL_PARENT = "NOTIFY_GLOBAL_PARENT";
    public static final String NOTIFY_PARENT = "NOTIFY_PARENT";
    public static final String NOTIFY_CHILD = "NOTIFY_CHILD";
    public static final String NOTIFY_ALL = "NOTIFY_ALL";

    public static final String FIRST_NODE = "FIRST_NODE";
    public static final String NODE = "NODE";
    public static final String ACCEPT_NODE = "ACCEPT_NODE";
    public static final String FREE_FIELD = "FREE_FIELD";
    public static final String ID = "ID";
    public static final String ID_MAP = "ID_MAP";

    public static final String NODE_1 = "1";
    public static final String NODE_2 = "2";

    public static final String CIRCLE_CHECK = "CIRCLE_CHECK";
}