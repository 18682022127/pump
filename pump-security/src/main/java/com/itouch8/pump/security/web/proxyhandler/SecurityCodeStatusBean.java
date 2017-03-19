package com.itouch8.pump.security.web.proxyhandler;

import java.io.Serializable;


public class SecurityCodeStatusBean implements Serializable {

    
    private static final long serialVersionUID = 7440779692067870455L;

    
    private String code;

    
    private String view;

    
    private String ajaxView;

    
    private int httpStatus;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getAjaxView() {
        return ajaxView;
    }

    public void setAjaxView(String ajaxView) {
        this.ajaxView = ajaxView;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
