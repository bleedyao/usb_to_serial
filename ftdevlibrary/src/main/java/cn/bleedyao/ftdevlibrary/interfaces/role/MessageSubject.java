package cn.bleedyao.ftdevlibrary.interfaces.role;


/**
 * Created by YaoLuHao on 2017/6/1 11:28.
 */

public interface MessageSubject {
    void addObserver(MessageObserver o);

    void removeObserver(MessageObserver o);

    void notifyObservers(String message);
}
