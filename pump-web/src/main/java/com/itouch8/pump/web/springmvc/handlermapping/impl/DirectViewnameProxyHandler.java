package com.itouch8.pump.web.springmvc.handlermapping.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.itouch8.pump.web.WebUtils;
import com.itouch8.pump.web.springmvc.handlermapping.IProxyHandler;


public class DirectViewnameProxyHandler implements IProxyHandler {

    private String suffix = "page";

    @Override
    public boolean isSupport(HttpServletRequest request) {
        String suffix = getSuffix();
        return request.getRequestURI().endsWith("." + suffix);
    }

    @Override
    public ModelAndView handler(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String requestUrl = request.getRequestURI();
        String view = WebUtils.getRestUriWithoutRoot(requestUrl);
        mv.setViewName(view);
        return mv;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
