package com.itouch8.pump.web.request.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itouch8.pump.core.service.request.IRequestInfo;


public interface IRequestLog {

    
    public void startRequestLog(HttpServletRequest request, HttpServletResponse response, IRequestInfo info);

    
    public void exceptionRequestLog(HttpServletRequest request, HttpServletResponse response, IRequestInfo info, Exception e, long endTime);

    
    public void endRequestLog(HttpServletRequest request, HttpServletResponse response, IRequestInfo info, long endTime);
}
