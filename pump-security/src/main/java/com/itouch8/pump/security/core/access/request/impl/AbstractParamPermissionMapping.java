package com.itouch8.pump.security.core.access.request.impl;

import java.util.Map;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.permission.IPermission;
import com.itouch8.pump.security.core.access.request.IRequestInfoPermissionMapping;


public abstract class AbstractParamPermissionMapping implements IRequestInfoPermissionMapping {

    private String paramName;

    @Override
    public IPermission lookup(IRequestInfo requestInfo) {
        String paramName = getParamName();
        Map<String, ?> parameters = requestInfo.getParameters();
        String param = this.getParameter(parameters, paramName);
        return getPermission(param);
    }

    
    protected abstract IPermission getPermission(String param);

    protected String getParameter(Map<String, ?> parameters, String name) {
        if (parameters.containsKey(name)) {
            Object p = parameters.get(name);
            if (null == p) {
                return null;
            } else if (p.getClass().isArray()) {
                Object[] arr = (Object[]) p;
                if (arr.length >= 1) {
                    return arr[0].toString();
                } else {
                    return null;
                }
            } else {
                return p.toString();
            }
        }
        return null;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
