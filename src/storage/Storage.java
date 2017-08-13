package storage;

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
    public static final int BOOTSTRAP_PORT = 1234;

    // New Node ---- > Server
    public static final String NEW = "NEW";

    // Server ---- > New Node ( if he is first )
    public static final String FIRST = "FIRST";

    // Server ---- > New Node ( if he is not first )
    public static final String NOT_FIRST = "NOT_FIRST";

    // New Node ---- > Node ( show me where to go )
    public static final String NEW_INFO = "NEW_INFO";

    // Node ---- > New Node ( if he is accepted )
    public static final String NEW_ACCEPT = "NEW_ACCEPT";
}
