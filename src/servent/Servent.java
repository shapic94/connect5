package servent;

import java.util.HashMap;

public class Servent {

    HashMap<String, String> list = new HashMap<String, String>();
    String id = null;
    int emptyLocalChild;
    int emptyGlobalChild;
    int fractalDegree;

    public Servent() {

    }

    public void updateList(String key, String value) {
        this.list.put(key, value);
    }

    public HashMap<String, String> getList() {
        return list;
    }

    public void setList(HashMap<String, String> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEmptyLocalChild() {
        return emptyLocalChild;
    }

    public void setEmptyLocalChild(int emptyLocalChild) {
        this.emptyLocalChild = emptyLocalChild;
    }

    public int getEmptyGlobalChild() {
        return emptyGlobalChild;
    }

    public void setEmptyGlobalChild(int emptyGlobalChild) {
        this.emptyGlobalChild = emptyGlobalChild;
    }

    public int getFractalDegree() {
        return fractalDegree;
    }

    public void setFractalDegree(int fractalDegree) {
        this.fractalDegree = fractalDegree;
    }
}
