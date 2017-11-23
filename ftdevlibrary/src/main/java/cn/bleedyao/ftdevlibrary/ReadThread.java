package cn.bleedyao.ftdevlibrary;

import android.os.Handler;
import android.os.Message;

import com.ftdi.j2xx.FT_Device;


/**
 * Created by yaoluhao on 15/11/2017.
 */
public class ReadThread extends Thread {
    private Handler mHandler;
    private boolean bReadThreadGoing;
    private FT_Device ftDev;
    long lastTime;
    long differTime;
    private static final int READ_DELAY = 50;
    private static final int CHECKOUT_DELAY = 60;

    ReadThread(Handler h, boolean bReadThreadGoing, FT_Device ftDev) {
        mHandler = h;
        this.bReadThreadGoing = bReadThreadGoing;
        this.ftDev = ftDev;
        this.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {
        int i;
        int iavailable;
        long currentTime;
        String temp = "";
        while (bReadThreadGoing) {
            try {
                Thread.sleep(READ_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                iavailable = ftDev.getQueueStatus();
                if (iavailable > 0) {
//                        Log.d(TAG, "iavailable: " + iavailable);
                    if (iavailable > ConfigParam.readLength) {
                        iavailable = ConfigParam.readLength;
                    }

                    ftDev.read(ConfigParam.readData, iavailable);
                    for (i = 0; i < iavailable; i++) {
                        ConfigParam.readDataToText[i] = (char) ConfigParam.readData[i];
                    }

                    currentTime = System.currentTimeMillis();
                    differTime = currentTime - lastTime;
                    lastTime = currentTime;

//                        Log.d(TAG, "run: " + differTime);
                    Message msg = mHandler.obtainMessage();
                    msg.what = ConfigParam.MESSAGE_RESEVIE;
                    msg.obj = extractData(ConfigParam.readDataToText, iavailable);
                    if (differTime > CHECKOUT_DELAY) {
                        temp = extractData(ConfigParam.readDataToText, iavailable);
                        mHandler.sendMessageDelayed(msg, CHECKOUT_DELAY);
                    } else {
                        mHandler.removeMessages(ConfigParam.MESSAGE_RESEVIE);
                        temp = temp.concat(extractData(ConfigParam.readDataToText,
                                iavailable));
                        msg.obj = temp;
                        mHandler.sendMessage(msg);
                    }
//                        Log.d(TAG, "run: " + temp);
                }
            }
        }
    }

    private String extractData(char[] charArray, int end) {
        return String.copyValueOf(charArray, 0, end);
    }

    public boolean isbReadThreadGoing() {
        return bReadThreadGoing;
    }

    public void setbReadThreadGoing(boolean bReadThreadGoing) {
        this.bReadThreadGoing = bReadThreadGoing;
    }
}
