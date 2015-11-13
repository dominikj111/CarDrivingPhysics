import java.awt.event.KeyEvent;
import java.util.HashMap;

public enum MonitoredKeys {
    UP(0, KeyEvent.VK_UP), DOWN(1, KeyEvent.VK_DOWN), LEFT(2, KeyEvent.VK_LEFT), RIGHT(3, KeyEvent.VK_RIGHT);

    public final int index;
    public final int keyCode;


    MonitoredKeys(int index, int keyCode){
        this.index = index;
        this.keyCode = keyCode;
    }

    public int getIndex(){return this.index;}

    public int getKeyCode(){return this.keyCode;}



    public static boolean isMonitored(int keyCode){
        return mapOfIndexes.containsKey(keyCode);
    }

    public static int getIndex(int keyCode){
        if(!isMonitored(keyCode)){return -1;}
        return mapOfIndexes.get(keyCode);
    }

    public static int getCountOfMonitoredKeys(){return MonitoredKeys.values().length;}


    private static HashMap<Integer, Integer> mapOfIndexes;

    static {
        mapOfIndexes = new HashMap<>();
        mapOfIndexes.put(MonitoredKeys.UP.getKeyCode(), MonitoredKeys.UP.getIndex());
        mapOfIndexes.put(MonitoredKeys.DOWN.getKeyCode(), MonitoredKeys.DOWN.getIndex());
        mapOfIndexes.put(MonitoredKeys.LEFT.getKeyCode(), MonitoredKeys.LEFT.getIndex());
        mapOfIndexes.put(MonitoredKeys.RIGHT.getKeyCode(), MonitoredKeys.RIGHT.getIndex());
    }
}
