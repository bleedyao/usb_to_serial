package cn.bleedyao.ftdevlibrary.exchange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import cn.bleedyao.ftdevlibrary.core.ConfigParam;
import cn.bleedyao.ftdevlibrary.interfaces.role.MessageObserver;
import cn.bleedyao.ftdevlibrary.interfaces.role.MessageSubject;

/**
 * Created by yaoluhao on 15/11/2017.
 * usb 转串口设备
 */

public class FtDev implements MessageSubject {
    private static FtDev mInstance;
    private ArrayList<MessageObserver> observers = null;
    private ConfigParam mConfig;

    @Override
    public void addObserver(MessageObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(MessageObserver o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers(String message) {
        for (int i = 0; i < observers.size(); i++) {
            MessageObserver observer = observers.get(i);
            if (observer.filter(message)) {
                observer.receive(message);
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
            mInstance.notifyObservers((String) msg.obj);
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

    public void sendMessage(String message) {
        mConfig.sendCommand(message);
    }

    /**
     * @auther YaoLuHao
     * create by 23/11/2017 11:09
     * 获得 handler
     */
    public static Handler getHandler() {
        return mHandler;
    }


//    private class Commander {
//        private ConfigParam mConfig;
//
//        // 接收到的报文数据
//        private String receiveData;
//        // 转换格式后的临时数据
//        private String temp;
//        // 接收报文数据更新时间
//        private long updateReceiveDateTime;
//        // 最后接收到的报文数据时间
//        private long lastReceiveDataTime;
//
//        Commander(ConfigParam config) {
//            mConfig = config;
//        }
//
//        public void sendMessage(String message) {
//            mConfig.sendCommand(message);
//        }
//
//        public Exchanger sendMessageWithConverter(String message, Formater formater) {
//            mConfig.sendCommand(converterFormat(message, formater));
//            return new Exchanger();
//        }
//
////        public Exchanger sendMessageWithSubscriber(String message,Subscriber subscriber){
////            return new Exchanger();
////        }
//
//        public Exchanger sendMessageWithSubscriberAndConverter(String message, Subscriber
//                subscriber, Formater formater) {
//            return new Exchanger();
//        }
//
//        public Exchanger addSubscricber(Subscriber subscriber) {
//            return addSubscricberWithConverter(subscriber, null);
//        }
//
//        /**
//         * @auther YaoLuHao
//         * create by 24/11/2017 09:49
//         * 1. 判断是否为最新获得的数据
//         * 2. 判断是否为需要的数据 subscriber
//         * 3. 转换
//         * 4. 下一步处理
//         */
//        public Exchanger addSubscricberWithConverter(Subscriber subscriber,
//                                                     Formater formater) {
//            if (!isLastestData()) {
//                return new Exchanger();
//            }
//            if (subscriber == null) {
//                return new Exchanger();
//            }
//            if (!subscriber.regular(receiveData)) {
//                return new Exchanger();
//            }
//            temp = converterFormat(receiveData, formater);
//            return new Exchanger();
//        }
//
//        private boolean isLastestData() {
//            if (updateReceiveDateTime <= lastReceiveDataTime) {
//                lastReceiveDataTime = updateReceiveDateTime;
//                return false;
//            } else {
//                lastReceiveDataTime = updateReceiveDateTime;
//                return true;
//            }
//
//        }
//
//        private String converterFormat(String message, Formater formater) {
//            if (formater != null) {
//                message = formater.transform(message);
//            }
//            return message;
//        }
//
//        public String getReceiveData() {
//            return receiveData;
//        }
//
//        void setReceiveData(String receiveData) {
//            synchronized (FtDev.class) {
//                this.receiveData = receiveData;
//                updateReceiveDateTime = SystemClock.elapsedRealtime();
//            }
//        }
//    }
//
//    private class Exchanger {
//        String receivedData;
//
//        public String getReceivedData() {
//            return receivedData;
//        }
//
//        public void setReceivedData(String receivedData) {
//            this.receivedData = receivedData;
//        }
//    }
}
