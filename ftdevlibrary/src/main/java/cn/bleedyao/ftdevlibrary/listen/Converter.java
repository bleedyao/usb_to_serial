package cn.bleedyao.ftdevlibrary.listen;

/**
 * Created by yaoluhao on 25/11/2017.
 */

public interface Converter {

    String convert(String message);

    String restore(String charMessage);
}
