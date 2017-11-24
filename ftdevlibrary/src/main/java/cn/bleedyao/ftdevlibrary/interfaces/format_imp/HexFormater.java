package cn.bleedyao.ftdevlibrary.interfaces.format_imp;

import cn.bleedyao.ftdevlibrary.interfaces.Formater;

/**
 * Created by yaoluhao on 23/11/2017.
 */

public class HexFormater implements Formater {

    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param command 原始指令字符串
     * @return 转换后的字符串
     */
    @Override
    public String transform(String command) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = command.getBytes();
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = b & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param transformedCommand 已经转换过的16进制字符串
     * @return 标准字符串
     */
    @Override
    public String restore(String transformedCommand) {
        String str = "0123456789ABCDEF";
        char[] hexs = transformedCommand.toCharArray();
        byte[] bytes = new byte[transformedCommand.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
}
