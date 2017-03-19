package com.itouch8.pump.web.path;

import javax.servlet.http.HttpServletRequest;


public interface IPathResolver {

    
    public String resolver(HttpServletRequest request, String path);
}
