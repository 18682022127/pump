package com.itouch8.pump.core.service.request;

import java.util.Date;
import java.util.Map;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 请求工厂<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public interface IRequestInfo {

    
    /**
     * 请求id
     * 
     * @return
     */
    public String getRequestId();

    
    /**
     * url
     * 
     * @return
     */
    public String getRequestUrl();

    
    /**
     * 协议
     * 
     * @return
     */
    public String getProtocol();

    
    /**
     * 客户端IP
     * 
     * @return
     */
    public String getRemoteIp();

    
    /**
     * 客户端操作系统
     * 
     * @return
     */
    public String getRemoteOperateSystem();

    
    /**
     * 客户端浏览器
     * 
     * @return
     */
    public String getRemoteBrowser();

    
    /**
     * 请求参数
     * 
     * @return
     */
    public Map<String, Object> getParameters();

    
    /**
     * 日期
     * 
     * @return
     */
    public Date getDate();

    
    /**
     * 时间
     * 
     * @return
     */
    public long getTime();
}
