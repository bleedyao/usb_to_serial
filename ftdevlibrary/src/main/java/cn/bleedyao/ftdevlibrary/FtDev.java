package cn.bleedyao.ftdevlibrary;

/**
 * Created by yaoluhao on 15/11/2017.
 * usb 转串口设备
 */

public class FtDev {
    private static FtDev instance;

    private FtDev() {

    }

    /**
     * @auther YaoLuHao
     * create by 15/11/2017 11:45
     * 获取设备的单一实例
     */
    public static FtDev getInstance() {
        if (instance == null) {
            synchronized (FtDev.class) {
                if (instance == null)
                    instance = new FtDev();
            }
        }
        return instance;
    }

    /**
      * @auther YaoLuHao
      * create by 15/11/2017 11:48
      * 发送指令
      */
    public void sendCommand() {

    }
}
