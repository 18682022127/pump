
package com.itouch8.pump.core.dao.log;


public class Utilities {
    
    public static String rightJustify(int fieldSize, String field) {
        if (field == null) {
            field = "";
        }
        StringBuffer output = new StringBuffer();
        for (int i = 0, j = fieldSize - field.length(); i < j; i++) {
            output.append(' ');
        }
        output.append(field);
        return output.toString();
    }

}
