package com.itouch8.pump.security.core.access.authz.impl;

import java.util.List;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.security.core.access.handler.IAccessControlHandler;
import com.itouch8.pump.security.core.access.info.IAuthorizationInfo;
import com.itouch8.pump.security.core.access.info.impl.BaseAuthorizationInfo;
import com.itouch8.pump.security.core.access.listener.IAccessControlListener;


public class BaseAuthorizer extends AbstractPathMatcherAuthorizer {

    
    private List<IAccessControlHandler> handlers;

    
    private List<IAccessControlListener> listeners;

    @Override
    protected IAuthorizationInfo doPermitted(IRequestInfo requestInfo) {
        BaseAuthorizationInfo info = new BaseAuthorizationInfo();
        List<IAccessControlListener> listeners = getListeners();
        try {
            if (null != listeners) {
                for (IAccessControlListener listener : listeners) {
                    listener.beforePermitted(requestInfo);
                }
            }
            this.handler(requestInfo, info);
            if (null != listeners) {
                boolean pass = info.isSuccess();
                for (IAccessControlListener listener : listeners) {
                    if (pass) {
                        listener.onPermittedPass(requestInfo, info);
                    } else {
                        listener.onPermittedDeny(requestInfo, info);
                    }
                }
            }
        } catch (Exception e) {
            if (null != listeners) {
                for (IAccessControlListener listener : listeners) {
                    listener.onPermittedException(requestInfo, info, e);
                }
            }
            e.printStackTrace();
        }
        return info;
    }

    private void handler(IRequestInfo requestInfo, IAuthorizationInfo info) {
        List<IAccessControlHandler> handlers = getHandlers();
        if (null != handlers) {
            for (IAccessControlHandler handler : handlers) {
                handler.handler(requestInfo, info);
                if (!info.isSuccess()) {
                    break;
                }
            }
        }
    }

    public List<IAccessControlHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<IAccessControlHandler> handlers) {
        this.handlers = handlers;
    }

    public List<IAccessControlListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<IAccessControlListener> listeners) {
        this.listeners = listeners;
    }
}
