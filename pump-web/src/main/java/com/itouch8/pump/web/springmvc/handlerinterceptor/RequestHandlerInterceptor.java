package com.itouch8.pump.web.springmvc.handlerinterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.servlet.filter.RequestFilter;
import com.itouch8.pump.web.view.ViewMappingHolder;


public class RequestHandlerInterceptor extends HandlerInterceptorAdapter {

    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServletHelp.setRequestAndResponse(request, response);
        return true;
    }

    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        IRequestInfo info = ServletHelp.getRequestInfo();
        if (null != info && null != modelAndView) {
            String overrideView = ViewMappingHolder.getOverrideView(info);
            if (!CoreUtils.isBlank(overrideView)) {
                modelAndView.setViewName(overrideView);
            }
            if (!modelAndView.hasView()) {
                String defaultView = ViewMappingHolder.getDefaultView(info);
                if (!CoreUtils.isBlank(defaultView)) {
                    modelAndView.setViewName(defaultView);
                }
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }

    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ServletHelp.remove();
    }
}
