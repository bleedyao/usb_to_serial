package cn.bleedyao.ftdevlibrary.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import cn.bleedyao.ftdevlibrary.listen.Converter;
import cn.bleedyao.ftdevlibrary.role.MessageObserver;
import cn.bleedyao.ftdevlibrary.role.MessageSubject;

/**
 * Created by yaoluhao on 15/11/2017.
 * usb 转串口设备
 */

public class FtDev implements MessageSubject {
    private static FtDev mInstance;
    private ArrayList<MessageObserver> observers = null;
    private ConfigParam mConfig;
    private Converter mConverter;
    static final String DATA = "data";
    static final String AVAILABLE = "available";

    @Override
    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MessageObserver observer) {
        int i = observers.indexOf(observer);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers(String message, int available) {
        for (int i = 0; i < observers.size(); i++) {
            MessageObserver observer = observers.get(i);
            if (observer.filter(message)) {
                if (mConverter != null) {
                    message = mConverter.restore(message);
                }
                observer.receive(message,available);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            String temp = (String) msg.obj;
//            // 接收到的数据
//            Log.d("ftDev", "handleMessage: " + temp);
            Bundle data = msg.getData();
            if (data == null) return;
            mInstance.notifyObservers(data.getString(DATA), data.getInt(AVAILABLE));
        }
    };

    private FtDev() {
        observers = new ArrayList<>();
    }

    private FtDev(Context context) {
        this();
        mConfig = new ConfigParam(context);
    }

    /**
     * @auther YaoLuHao
     * create by 15/11/2017 11:45
     * 初始化 FtDev 对象
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (FtDev.class) {
                if (mInstance == null)
                    mInstance = new FtDev(context);
            }
        }
    }

    public static FtDev getInstance() {
        if (mInstance == null)
            throw new RuntimeException("FtDev 还没有初始化，请先调用 init() 方法初始化该对象。");
        else
            return mInstance;
    }

    public void close() {
        mConfig.disconnectFunction();
    }

    public void sendMessage(String message) {
        mConfig.sendCommand(message, mConverter);
    }


    public int getBaudRate() {
        return mConfig.baudRate;
    }

    public FtDev setBaudRate(int baudRate) {
        mConfig.baudRate = baudRate;
        return this;
    }

    public byte getStopBit() {
        return mConfig.stopBit;
    }

    public FtDev setStopBit(byte stopBit) {
        mConfig.stopBit = stopBit;
        return this;
    }

    public byte getDataBit() {
        return mConfig.dataBit;
    }

    public FtDev setDataBit(byte dataBit) {
        mConfig.dataBit = dataBit;
        return this;
    }

    public byte getParity() {
        return mConfig.parity;
    }

    public FtDev setParity(byte parity) {
        mConfig.parity = parity;
        return this;
    }

    public byte getFlowControl() {
        return mConfig.flowControl;
    }

    public FtDev setFlowControl(byte flowControl) {
        mConfig.flowControl = flowControl;
        return this;
    }

    public FtDev setConvertModel(Converter converter) {
        this.mConverter = converter;
        return this;
    }

    public void clearConvertModel() {
        this.mConverter = null;
    }

    public String getCharsetName() {
        return mConfig.charsetName;
    }

    public FtDev setCharsetName(String charsetName) {
        mConfig.charsetName = charsetName;
        return this;
    }

    /**
     * @auther YaoLuHao
     * create by 23/11/2017 11:09
     * 获得 handler
     */
    public static Handler getHandler() {
        return mHandler;
    }

}
