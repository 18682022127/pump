package com.itouch8.pump.web.springmvc.handlermapping;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;


public interface IProxyHandler {

    
    public boolean isSupport(HttpServletRequest request);

    
    public ModelAndView handler(HttpServletRequest request);
}
