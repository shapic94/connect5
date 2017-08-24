package servent;

public class ServentSingleton extends Servent {
    private static ServentSingleton myInstance;

    private ServentSingleton() {

    }

    public static ServentSingleton getInstance() {
        if (myInstance == null) {
            myInstance = new ServentSingleton();
        }

        return myInstance;
    }
}
