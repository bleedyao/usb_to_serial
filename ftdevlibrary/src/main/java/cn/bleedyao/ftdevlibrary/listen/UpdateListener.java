package cn.bleedyao.ftdevlibrary.listen;

/**
 * Created by yaoluhao on 27/11/2017.
 */

public interface UpdateListener<T> {
    void success(T list);

    void fail();
}
