package cn.bleedyao.ftdevlibrary.interfaces;

/**
 * Created by yaoluhao on 23/11/2017.
 */

public interface Formater {
    String transform(String command);
    String restore(String transformedCommand);
}
