package com.itouch8.pump.core.service.request;

import java.util.Date;
import java.util.Map;


public interface IRequestInfo {

    
    public String getRequestId();

    
    public String getRequestUrl();

    
    public String getProtocol();

    
    public String getRemoteIp();

    
    public String getRemoteOperateSystem();

    
    public String getRemoteBrowser();

    
    public Map<String, Object> getParameters();

    
    public Date getDate();

    
    public long getTime();
}
