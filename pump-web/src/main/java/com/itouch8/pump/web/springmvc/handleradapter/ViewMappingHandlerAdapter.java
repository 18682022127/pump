package com.itouch8.pump.web.springmvc.handleradapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import com.itouch8.pump.web.springmvc.handlermapping.ViewMappingHandlerMapping;


@Component
public class ViewMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ViewMappingHandlerMapping.ViewMappingRequestHandler);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return ((ViewMappingHandlerMapping.ViewMappingRequestHandler) handler).handler();
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return -1L;
    }

}
