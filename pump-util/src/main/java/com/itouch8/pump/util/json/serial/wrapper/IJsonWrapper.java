package com.itouch8.pump.util.json.serial.wrapper;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : Json数据包装接口<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public interface IJsonWrapper {
    
    /**
     * 包装
     * 
     * @param original
     * @return
     */
    public Object wrap(Object original);
}
