package com.itouch8.pump.web.springmvc.download.builder;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IDownloadObjectBuilder {

    
    public boolean isSingleon();

    
    public int getOrder();

    
    public String getBuildType();

    
    public Object build(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response);
}
