package cn.bleedyao.ftdevlibrary.lamp;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import cn.bleedyao.ftdevlibrary.core.FtDev;
import cn.bleedyao.ftdevlibrary.listen.UpdateListener;
import cn.bleedyao.ftdevlibrary.role.MessageObserver;

/**
 * Created by yaoluhao on 27/11/2017.
 */

public class LampConfig implements MessageObserver {
    private static final String TAG = "LampConfig";
    private int maxAvailable = -1;
    private String lastUpdated = "";
    private UpdateListener<String, Map<String, Integer>> listener;
    private Map<String, Integer> history;
    private ReceiceFilter filter;
    private int responseCount = 0;

    private LampConfig() {
        history = new HashMap<>();
    }

    private static final LampConfig INTANCE = new LampConfig();

    public static LampConfig getIntance() {
        return INTANCE;
    }

    public static void init(Context context) {
        FtDev.init(context);
    }

    public void addObserver() {
        FtDev.getInstance().addObserver(this);
    }

    public LampConfig setFilter(ReceiceFilter filter) {
        this.filter = filter;
        return this;
    }

    public LampConfig setUpdateListenr(UpdateListener<String, Map<String, Integer>> listenr) {
        this.listener = listenr;
        return this;
    }

    public void sendMessage(String msg) {
        FtDev.getInstance().sendMessage(msg);
    }

    public Map<String, Integer> getHistoryData() {
        return history;
    }

    public void clearListenerAndFilter() {
        clearFilter();
        clearListener();
    }

    public void clearListener() {
        if (listener == null) {
            listener = null;
        }
    }

    public void clearFilter() {
        if (filter == null) {
            filter = null;
        }
    }

    public void clearHistory() {
        if (history != null) {
            history.clear();
        }
    }

    @Override
    public boolean filter(String message) {
        return filter == null || filter.customFilter(message);
    }

    @Override
    public void receive(String message, int available) {
        available *= 2;
        if (available > maxAvailable) {
            maxAvailable = available;
        }
        if (message.length() > maxAvailable) {
//            Log.d(TAG, "receive: " + maxAvailable);
            return;
        }
        if (history.containsKey(message)) {
            int temp = history.get(message);
            history.put(message, temp + 1);
        } else {
            history.put(message, 1);
        }
//        Log.d(TAG, "receive: " + message);
        if (listener != null) {
            lastUpdated = message;
            listener.lastest(lastUpdated);
            listener.history(history);
        }
    }
}
