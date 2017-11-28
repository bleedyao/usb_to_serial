package cn.bleedyao.ftdevlibrary.listen;

/**
 * Created by yaoluhao on 27/11/2017.
 */

public interface UpdateListener<T, V> {
    void lastest(T message);

    void history(V history);

}
