package servent;

import java.util.HashMap;

public class Servent {

    HashMap<Integer, String> list = new HashMap<Integer, String>();

    public Servent() {

    }

    public void updateList(int key, String value) {
        this.list.put(key, value);
    }

    public HashMap<Integer, String> getList() {
        return list;
    }

    public void setList(HashMap<Integer, String> list) {
        this.list = list;
    }
}
