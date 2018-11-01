package com.itouch8.pump.core.service.request.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.itouch8.pump.core.service.request.IRequestInfo;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 请求信息的基础实现<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class BaseRequestInfo implements IRequestInfo {

    
    private String requestId;

    
    private String requestUrl;

    
    private String protocol;

    
    private String remoteIp;

    
    private String remoteOperateSystem;

    
    private String remoteBrowser;

    
    private Map<String, Object> parameters;

    
    private Date date;

    
    private long time;

    
    @Override
    public String getRequestId() {
        return this.requestId;
    }

    
    @Override
    public String getRequestUrl() {
        return this.requestUrl;
    }

    
    @Override
    public String getProtocol() {
        return this.protocol;
    }

    
    @Override
    public String getRemoteIp() {
        return this.remoteIp;
    }

    
    @Override
    public String getRemoteOperateSystem() {
        return this.remoteOperateSystem;
    }

    
    @Override
    public String getRemoteBrowser() {
        return this.remoteBrowser;
    }

    
    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    
    @Override
    public Date getDate() {
        return this.date;
    }

    
    @Override
    public long getTime() {
        return this.time;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public void setRemoteOperateSystem(String remoteOperateSystem) {
        this.remoteOperateSystem = remoteOperateSystem;
    }

    public void setRemoteBrowser(String remoteBrowser) {
        this.remoteBrowser = remoteBrowser;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void addParameters(String key, Object parameter) {
        Map<String, Object> parameters = getParameters();
        if (null == parameters) {
            parameters = new LinkedHashMap<String, Object>();
        }
        parameters.put(key, parameter);
    }

    public void addAllParameters(Map<String, Object> parameters) {
        Map<String, Object> params = this.getParameters();
        if (null == parameters) {
            parameters = new LinkedHashMap<String, Object>();
        }
        params.putAll(parameters);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
