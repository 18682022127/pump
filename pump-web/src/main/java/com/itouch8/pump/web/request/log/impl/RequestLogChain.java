package com.itouch8.pump.web.request.log.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.request.log.IRequestLog;


public class RequestLogChain implements IRequestLog {

    
    @Override
    public void startRequestLog(final HttpServletRequest request, final HttpServletResponse response, final IRequestInfo info) {
        List<IRequestLog> requestLogs = WebPumpConfig.getRequestLogs();
        if (null != requestLogs && !requestLogs.isEmpty()) {
            for (IRequestLog log : requestLogs) {// 顺序打印开始日志
                try {
                    log.startRequestLog(request, response, info);
                } catch (Throwable ignore) {// 忽略日志本身的异常
                }
            }
        }
    }

    @Override
    public void exceptionRequestLog(HttpServletRequest request, HttpServletResponse response, IRequestInfo info, Exception e, long endTime) {
        List<IRequestLog> requestLogs = WebPumpConfig.getRequestLogs();
        if (null != requestLogs && !requestLogs.isEmpty()) {
            for (IRequestLog log : requestLogs) {// 顺序打印异常日志
                try {
                    log.exceptionRequestLog(request, response, info, e, endTime);
                } catch (Throwable ignore) {// 忽略日志本身的异常
                }
            }
        }

    }

    
    @Override
    public void endRequestLog(final HttpServletRequest request, final HttpServletResponse response, final IRequestInfo info, final long endTime) {
        List<IRequestLog> requestLogs = WebPumpConfig.getRequestLogs();
        if (null != requestLogs && !requestLogs.isEmpty()) {
            for (int i = requestLogs.size() - 1; i >= 0; i--) {// 逆序打印结束日志
                try {
                    requestLogs.get(i).endRequestLog(request, response, info, endTime);
                } catch (Throwable ignore) {// 忽略日志本身的异常
                }
            }
        }
    }
}
