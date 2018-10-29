package com.itouch8.pump.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itouch8.pump.core.dao.mybatis.mapper.impl.DataStreamMapperMethodExecutor;
import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.track.Tracker;
import com.itouch8.pump.web.annotation.JsonBodySupport;
import com.itouch8.pump.web.request.log.IRequestLog;
import com.itouch8.pump.web.request.log.impl.RequestLogChain;
import com.itouch8.pump.web.servlet.ServletHelp;

public class RequestFilter extends AbstractSkipPathMatcherFilter {

    private static final IRequestLog log = new RequestLogChain();

    @Override
    protected void executeFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Tracker.start();
        ServletHelp.setRequestAndResponse(request, response);
        IRequestInfo info = ServletHelp.getRequestInfo();
        log.startRequestLog(request, response, info);
        try {
            filterChain.doFilter(request, response);
            log.endRequestLog(request, response, info, System.currentTimeMillis());
        } catch (Exception e) {
            log.exceptionRequestLog(request, response, info, e, System.currentTimeMillis());
            throw e;
        } finally {
            CoreUtils.clearThreadCache();
            JsonBodySupport.removeJsonBodyInfoFormContext();
            DataStreamMapperMethodExecutor.clearDataStreamReader();
            ServletHelp.remove();
            Tracker.stop();
        }
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        ServletHelp.setApplication(super.getServletContext());
    }
}
