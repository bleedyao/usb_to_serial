package cn.bleedyao.ftdevlibrary.listen;

/**
 * Created by yaoluhao on 25/11/2017.
 */

public class HexConvertModel implements Converter {
    @Override
    public String convert(String hex) {
        StringBuilder sb = new StringBuilder();
//        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

//            temp.append(decimal);
        }

        return sb.toString();
    }

    @Override
    public String restore(String charMessage) {
        StringBuilder sb = new StringBuilder();
        String temp = "";
        // 转回 16 进制
        for (char ch : charMessage.toCharArray()) {
            temp = Integer.toHexString(ch & 0xff);
            if (temp.length() == 1) {
                temp = "0".concat(temp);
            }
            sb.append(temp);
        }
        return sb.toString();
    }
}
