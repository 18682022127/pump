package com.itouch8.pump.security.web.proxyhandler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.logger.CommonLogger;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.web.WebUtils;
import com.itouch8.pump.web.servlet.ServletHelp;
import com.itouch8.pump.web.springmvc.handlermapping.IProxyHandler;


public class SecurityProxyHandler implements IProxyHandler {

    
    private String authorizationInfoAttributeName = "authorizationInfo";

    
    private Map<String, SecurityCodeStatusBean> codeStatusMapping;

    
    private String defaultView;

    
    private String ajaxDefaultView;

    @Override
    public boolean isSupport(HttpServletRequest request) {
        return null != request.getAttribute(IAuthorizationInfo.class.getName());
    }

    @Override
    public ModelAndView handler(HttpServletRequest request) {
        IAuthorizationInfo info = (IAuthorizationInfo) request.getAttribute(IAuthorizationInfo.class.getName());
        request.removeAttribute(IAuthorizationInfo.class.getName());
        String code = info.getCode();
        CommonLogger.warn("access deny [" + code + "] : " + info.getMessage());

        Map<String, SecurityCodeStatusBean> codeStatusMapping = getCodeStatusMapping();
        SecurityCodeStatusBean status = codeStatusMapping == null ? null : codeStatusMapping.get(code);

        if (isAjaxRequest(request)) {
            return doHandlerAjaxRequest(info, status);
        } else {
            return doHandlerNormalRequest(info, status);
        }
    }

    
    protected ModelAndView doHandlerNormalRequest(IAuthorizationInfo info, SecurityCodeStatusBean status) {
        ModelAndView mv = new ModelAndView();
        mv.addObject(getAuthorizationInfoAttributeName(), info);

        String view = status == null ? null : status.getView();
        if (CoreUtils.isBlank(view)) {
            view = getDefaultView();
        }
        mv.setViewName(view);

        if (null != status && 0 != status.getHttpStatus()) {
            try {
                HttpServletResponse response = ServletHelp.getResponse();
                response.sendError(status.getHttpStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mv;
    }

    
    protected ModelAndView doHandlerAjaxRequest(IAuthorizationInfo info, SecurityCodeStatusBean status) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("success", info.isSuccess());
        Map<String, Object> data = new HashMap<String, Object>();
        mv.addObject("data", data);
        data.put("code", info.getCode());
        data.put("message", info.getMessage());
        String view = status == null ? null : status.getAjaxView();
        if (CoreUtils.isBlank(view)) {
            view = getAjaxDefaultView();
        }
        mv.setViewName(view);
        return mv;
    }

    
    protected boolean isAjaxRequest(HttpServletRequest request) {
        return WebUtils.isAjaxRequest(request);
    }

    public String getAuthorizationInfoAttributeName() {
        return authorizationInfoAttributeName;
    }

    public void setAuthorizationInfoAttributeName(String authorizationInfoAttributeName) {
        this.authorizationInfoAttributeName = authorizationInfoAttributeName;
    }

    public Map<String, SecurityCodeStatusBean> getCodeStatusMapping() {
        return codeStatusMapping;
    }

    public void setCodeStatusMapping(Map<String, SecurityCodeStatusBean> codeStatusMapping) {
        this.codeStatusMapping = codeStatusMapping;
    }

    public String getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
    }

    public String getAjaxDefaultView() {
        return ajaxDefaultView;
    }

    public void setAjaxDefaultView(String ajaxDefaultView) {
        this.ajaxDefaultView = ajaxDefaultView;
    }
}
