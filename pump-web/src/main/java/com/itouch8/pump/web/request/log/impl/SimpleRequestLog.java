package com.itouch8.pump.web.request.log.impl;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.request.log.IParamConvert;
import com.itouch8.pump.web.request.log.IRequestLog;


public class SimpleRequestLog implements IRequestLog {

    
    @Override
    public void startRequestLog(HttpServletRequest request, HttpServletResponse response, IRequestInfo info) {
        StringBuffer sb = new StringBuffer().append("requestId=").append(info.getRequestId()).append(",requestUrl=").append(info.getRequestUrl()).append(",remoteIp=").append(info.getRemoteIp()).append(",remoteOs=").append(info.getRemoteOperateSystem()).append(",remoteBrowser=").append(info.getRemoteBrowser());
        try {
            Enumeration<String> a = request.getParameterNames();
            if (null != a) {
                IParamConvert convert = WebPumpConfig.getParamConvert();
                while (a.hasMoreElements()) {
                    String name = a.nextElement();
                    String param = request.getParameter(name);
                    if (null != convert) {
                        param = convert.convert(name, param);
                    }
                    sb.append(",").append(name).append("=").append(param);
                }
            }
        } catch (Exception ignore) {
        }
        CommonLogger.info("the request start. ==> " + sb.toString());
    }

    
    @Override
    public void exceptionRequestLog(HttpServletRequest request, HttpServletResponse response, IRequestInfo info, Exception e, long endTime) {
        StringBuffer sb = new StringBuffer().append("requestId=").append(info.getRequestId()).append(",requestUrl=").append(info.getRequestUrl());
        CommonLogger.info("the request failure in " + (endTime - info.getTime()) + " ms. ==> " + sb.toString(), e);
    }

    
    @Override
    public void endRequestLog(HttpServletRequest request, HttpServletResponse response, IRequestInfo info, long endTime) {
        StringBuffer sb = new StringBuffer().append("requestId=").append(info.getRequestId()).append(",requestUrl=").append(info.getRequestUrl());
        CommonLogger.info("the request completed in " + (endTime - info.getTime()) + " ms. ==> " + sb.toString());
    }
}
