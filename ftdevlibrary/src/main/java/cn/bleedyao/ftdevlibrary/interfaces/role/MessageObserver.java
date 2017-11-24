package cn.bleedyao.ftdevlibrary.interfaces.role;

/**
 * Created by YaoLuHao on 2017/6/1 11:29.
 */

public interface MessageObserver {
    boolean filter(String message);

    void receive(String message);
}
