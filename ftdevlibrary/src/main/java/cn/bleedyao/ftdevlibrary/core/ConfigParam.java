package cn.bleedyao.ftdevlibrary.core;

import android.content.Context;
import android.util.Log;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.nio.charset.Charset;

import cn.bleedyao.ftdevlibrary.listen.Converter;
import cn.bleedyao.ftdevlibrary.utils.SingleToast;

/**
 * Created by yaoluhao on 21/11/2017.
 */

public class ConfigParam {
    static final int readLength = 512;
    static byte[] readData = new byte[readLength];
    static char[] readDataToText = new char[readLength];
    static final int MESSAGE_RESEVIE = 1;
    String charsetName = "ISO-8859-1";


    int baudRate; /*baud rate*/
    byte stopBit; /*1:1stop bits, 2:2 stop bits*/
    byte dataBit; /*8:8bit, 7: 7bit*/
    byte parity;  /* 0: none, 1: odd, 2: even, 3: mark, 4: space*/
    byte flowControl; /*0:none, 1: flow control(CTS,RTS)*/

    private D2xxManager ftD2xx;
    private FT_Device ftDev = null;
    private int devCount = -1;
    private int currentIndex = -1;
    private Context mContext;

    private boolean uart_configured = false;
    static boolean bReadThreadGoing;

    ConfigParam(Context context) {
        this.mContext = context.getApplicationContext();
        initParam();
        openUsbPort();
    }

    /**
     * @auther YaoLuHao
     * create by 23/11/2017 16:21
     * 打开usb端口，准备发送指令
     */
    private void openUsbPort() {
        createDeviceList();
        if (devCount > 0) {
            connectFunction();
            SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
        }
    }

    private void createDeviceList() {
        devCount = 0;
        int tempDevCount = ftD2xx.createDeviceInfoList(mContext);

        if (tempDevCount > 0) {
            if (devCount != tempDevCount) {
                devCount = tempDevCount;
            }
        } else {
            devCount = -1;
            currentIndex = -1;
        }
    }

    private void connectFunction() {
        int openIndex = 0;
        int tmpProtNumber = openIndex + 1;

        if (currentIndex != openIndex) {
            if (null == ftDev) {
                ftDev = ftD2xx.openByIndex(mContext, openIndex);
            } else {
                synchronized (this) {
                    ftDev = ftD2xx.openByIndex(mContext, openIndex);
                }
            }
            uart_configured = false;
        } else {
            SingleToast.showToastShort(mContext, "Device port " + tmpProtNumber + " is already " +
                    "opened");
            return;
        }

        if (ftDev == null) {
            SingleToast.showToastShort(mContext, "open device port(" + tmpProtNumber + ") NG, " +
                    "ftDev == null");
            return;
        }

        if (ftDev.isOpen()) {
            currentIndex = openIndex;
            SingleToast.showToastShort(mContext, "open device port(" + tmpProtNumber + ") OK");

            // 打开Usb端口时候，开启一个线程。
            if (!bReadThreadGoing) {
                ReadThread read_thread = new ReadThread(FtDev.getHandler(), ftDev);
                read_thread.start();
                bReadThreadGoing = true;
            }
        } else {
            SingleToast.showToastShort(mContext, "open device port(" + tmpProtNumber + ") NG");

            //Toast.makeText(DeviceUARTContext, "Need to get permission!", Toast.LENGTH_SHORT)
            // .show();
        }
    }

    private void SetConfig(int baud, byte dataBits, byte stopBits, byte parity, byte flowControl) {
        if (!ftDev.isOpen()) {
            Log.e("j2xx", "SetConfig: device not open");
            return;
        }

        // configure our port
        // reset to UART mode for 232 devices
        ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

        ftDev.setBaudRate(baud);

        switch (dataBits) {
            case 7:
                dataBits = D2xxManager.FT_DATA_BITS_7;
                break;
            case 8:
                dataBits = D2xxManager.FT_DATA_BITS_8;
                break;
            default:
                dataBits = D2xxManager.FT_DATA_BITS_8;
                break;
        }

        switch (stopBits) {
            case 1:
                stopBits = D2xxManager.FT_STOP_BITS_1;
                break;
            case 2:
                stopBits = D2xxManager.FT_STOP_BITS_2;
                break;
            default:
                stopBits = D2xxManager.FT_STOP_BITS_1;
                break;
        }

        switch (parity) {
            case 0:
                parity = D2xxManager.FT_PARITY_NONE;
                break;
            case 1:
                parity = D2xxManager.FT_PARITY_ODD;
                break;
            case 2:
                parity = D2xxManager.FT_PARITY_EVEN;
                break;
            case 3:
                parity = D2xxManager.FT_PARITY_MARK;
                break;
            case 4:
                parity = D2xxManager.FT_PARITY_SPACE;
                break;
            default:
                parity = D2xxManager.FT_PARITY_NONE;
                break;
        }

        ftDev.setDataCharacteristics(dataBits, stopBits, parity);

        short flowCtrlSetting;
        switch (flowControl) {
            case 0:
                flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
                break;
            case 1:
                flowCtrlSetting = D2xxManager.FT_FLOW_RTS_CTS;
                break;
            case 2:
                flowCtrlSetting = D2xxManager.FT_FLOW_DTR_DSR;
                break;
            case 3:
                flowCtrlSetting = D2xxManager.FT_FLOW_XON_XOFF;
                break;
            default:
                flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
                break;
        }

        // TODO : flow ctrl: XOFF/XOM
        // TODO : flow ctrl: XOFF/XOMthis
        ftDev.setFlowControl(flowCtrlSetting, (byte) 0x0b, (byte) 0x0d);

        uart_configured = true;
        SingleToast.showToastShort(mContext, "Config done");
    }

    private void initParam() {
        try {
            ftD2xx = D2xxManager.getInstance(mContext);
        } catch (D2xxManager.D2xxException ex) {
            ex.printStackTrace();
        }

    /* by default it is 9600 */
        baudRate = 9600;
        /* default is stop bit 1 */
        stopBit = 1;
        /* default data bit is 8 bit */
        dataBit = 8;
        /* default is none */
        parity = 0;
        /* default flow control is is none */
        flowControl = 0;
//        int portNumber = 1;


    }

    private void sendCommand(String command, String charsetName) {
        openUsbPort();
        if (ftDev == null) {
            SingleToast.showToastShort(mContext, "device is detached");
            return;
        }
        if (!ftDev.isOpen()) {
            SingleToast.showToastShort(mContext, "device is not open");
            return;
        }

        if (command == null) {
            SingleToast.showToastShort(mContext, "Command can't be null");
            return;
        }

        ftDev.setLatencyTimer((byte) 16);
//		ftDev.purge((byte) (D2xxManager.FT_PURGE_TX | D2xxManager.FT_PURGE_RX));

//        String writeData = writeText.getText().toString();
        byte[] outData = command.getBytes(Charset.forName(charsetName));
        SingleToast.showToastShort(mContext, "command length: "+command.length() + " send: ".concat(command));
//        Log.d("config", "sendCommand: " + Arrays.toString(outData));
        ftDev.write(outData, command.length());
    }

    void sendCommand(String message, Converter converter) {
        if (converter != null) {
            message = converter.convert(message);
        }
        sendCommand(message, charsetName);
    }

    void disconnectFunction() {
        devCount = -1;
        currentIndex = -1;
        bReadThreadGoing = false;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ftDev != null) {
            synchronized (this) {
                if (ftDev.isOpen()) {
                    ftDev.close();
                }
            }
        }
    }
}
