package cn.bleedyao.ftdevlibrary.lamp;

import cn.bleedyao.ftdevlibrary.role.MessageObserver;

/**
 * Created by yaoluhao on 27/11/2017.
 */

public class LampConfig implements MessageObserver {


    @Override
    public boolean filter(String message) {
        return false;
    }

    @Override
    public void receive(String message, int available) {

    }
}
