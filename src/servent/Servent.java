package servent;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Servent {

//    HashMap<String, String> list = new HashMap<String, String>();
    ConcurrentHashMap<String, String> list = new ConcurrentHashMap<String, String>();
    ConcurrentHashMap<String, String> proccess = new ConcurrentHashMap<String, String>();
    String id = null;
    int emptyLocalChild;
    int emptyGlobalChild;
    int fractalDegree;
    int resultPlayer1;
    int resultPlayer2;
    int localResultPlayer1;
    int localResultPlayer2;
    String izigravanje;

    boolean playing;

    String player1;
    String player2;
    int row;
    int col;
    int tokens;
    int times;
    int tempTimes;

    long loadTime = 0;

    public Servent() {

    }

    public void updateList(String key, String value) {
        this.list.put(key, value);
    }

    public ConcurrentHashMap<String, String> getList() {
        return list;
    }

    public void setList(ConcurrentHashMap<String, String> list) {
        this.list = list;
    }

    public void updateProccess(String key, String value) {
        this.proccess.put(key, value);
    }

    public ConcurrentHashMap<String, String> getProccess() {
        return proccess;
    }

    public void setProccess(ConcurrentHashMap<String, String> proccess) {
        this.proccess = proccess;
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

    public int getResultPlayer1() {
        return resultPlayer1;
    }

    public void setResultPlayer1(int resultPlayer1) {
        this.resultPlayer1 = resultPlayer1;
    }

    public int getResultPlayer2() {
        return resultPlayer2;
    }

    public void setResultPlayer2(int resultPlayer2) {
        this.resultPlayer2 = resultPlayer2;
    }

    public String getIzigravanje() {
        return izigravanje;
    }

    public void setIzigravanje(String izigravanje) {
        this.izigravanje = izigravanje;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getLocalResultPlayer1() {
        return localResultPlayer1;
    }

    public void setLocalResultPlayer1(int localResultPlayer1) {
        this.localResultPlayer1 = localResultPlayer1;
    }

    public int getLocalResultPlayer2() {
        return localResultPlayer2;
    }

    public void setLocalResultPlayer2(int localResultPlayer2) {
        this.localResultPlayer2 = localResultPlayer2;
    }

    public int getTempTimes() {
        return tempTimes;
    }

    public void setTempTimes(int tempTimes) {
        this.tempTimes = tempTimes;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
