package com.itouch8.pump.core.util.encrypt;


public interface IEncrypt {

    
    String encode(String data, String key);

    
    String decode(String data, String key);

}
