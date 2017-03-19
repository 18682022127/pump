package com.itouch8.pump.web.request.log.impl;

import java.util.HashSet;
import java.util.Set;

import com.itouch8.pump.web.request.log.IParamConvert;


public class SensitivesParamConvert implements IParamConvert {

    
    private Set<String> sensitiveNames;

    
    private char replaceChar = '*';

    @Override
    public String convert(String name, String src) {
        if (null == name || null == src || sensitiveNames == null || !sensitiveNames.contains(name.toLowerCase())) {
            return src;
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = src.length() - 1; i > 0; i--) {
                sb.append(replaceChar);
            }
            return sb.toString();
        }
    }

    public Set<String> getSensitiveNames() {
        return sensitiveNames;
    }

    public void setSensitiveNames(Set<String> sensitiveNames) {
        if (null != sensitiveNames) {
            this.sensitiveNames = new HashSet<String>();
            for (String name : sensitiveNames) {
                this.sensitiveNames.add(name.toLowerCase());
            }
        }
    }

    public char getReplaceChar() {
        return replaceChar;
    }

    public void setReplaceChar(char replaceChar) {
        this.replaceChar = replaceChar;
    }
}
